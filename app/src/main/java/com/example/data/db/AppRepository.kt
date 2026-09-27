package com.example.data.db

import android.content.Context
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class AppRepository(private val db: AppDatabase, private val context: Context) {

    private val firestore by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    private val userDao = db.userDao()
    private val groupDao = db.groupDao()
    private val transactionDao = db.transactionDao()
    private val activityLogDao = db.activityLogDao()
    private val travelPlanDao = db.travelPlanDao()
    private val challengeDao = db.challengeDao()

    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    val groupInfo: Flow<GroupInfo?> = groupDao.getGroupInfo()
    val allTransactions: Flow<List<SavingsTransaction>> = transactionDao.getAllTransactions()
    val activityLogs: Flow<List<ActivityLog>> = activityLogDao.getAllLogs()
    val travelPlan: Flow<TravelPlan?> = travelPlanDao.getTravelPlan()
    val challengeWeeks: Flow<List<ChallengeWeek>> = challengeDao.getAllWeeks()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        val existingGroup = groupDao.getGroupInfoSync()
        if (existingGroup == null) {
            val defaultGroup = GroupInfo(
                groupCode = "BOT-GEO-7K29",
                groupName = "Грузия — Наша цель 🇬🇪",
                targetAmount = 30000.0,
                currency = "TJS",
                streakCalculation = "DAILY",
                isCompleted = false,
                targetMonths = 5,
                monthlyTarget = 1000.0
            )
            groupDao.insertOrUpdateGroup(defaultGroup)

            val users = listOf(
                User(
                    id = "mustafa",
                    name = "Мустафо Тоиров",
                    email = "toirovm123@gmail.com",
                    passwordHash = "123456",
                    pinCode = "1234",
                    googleEmail = "toirovm123@gmail.com",
                    isGoogleLinked = true,
                    role = "ADMIN",
                    avatarType = "PRESET",
                    avatarValue = "mustafa",
                    personalGoal = 10000.0,
                    currentStreak = 0,
                    maxStreak = 0,
                    monthlyWins = 0
                ),
                User(
                    id = "ayub",
                    name = "Аюб",
                    email = "ayub@mail.com",
                    passwordHash = "123456",
                    pinCode = "1234",
                    googleEmail = "ayub@gmail.com",
                    isGoogleLinked = false,
                    role = "MEMBER",
                    avatarType = "PRESET",
                    avatarValue = "ayub",
                    personalGoal = 10000.0,
                    currentStreak = 0,
                    maxStreak = 0,
                    monthlyWins = 0
                ),
                User(
                    id = "muhammadsharif",
                    name = "Муҳаммадшариф",
                    email = "muhammad@mail.com",
                    passwordHash = "123456",
                    pinCode = "1234",
                    googleEmail = "muhammad@gmail.com",
                    isGoogleLinked = false,
                    role = "MEMBER",
                    avatarType = "PRESET",
                    avatarValue = "muhammad",
                    personalGoal = 10000.0,
                    currentStreak = 0,
                    maxStreak = 0,
                    monthlyWins = 0
                )
            )
            userDao.insertUsers(users)

            val now = System.currentTimeMillis()
            // Clean start: No dummy transactions, starts at 0 TJS
            val logs = listOf(
                ActivityLog(
                    timestamp = now,
                    actorName = "Мустафо",
                    actionType = "SETTING",
                    details = "Гурӯҳи нави 'Bank of Travel 🇬🇪 2026' фаъол карда шуд. Маблағ аз 0 TJS оғоз меёбад."
                )
            )
            for (log in logs) {
                activityLogDao.insertLog(log)
            }

            travelPlanDao.insertOrUpdate(TravelPlan())

            // 40 weeks challenge starts clean at 0.0 TJS
            val weeks = (1..40).map { w ->
                ChallengeWeek(
                    weekNumber = w,
                    targetAmount = 750.0,
                    savedAmount = 0.0,
                    isCompleted = false
                )
            }
            challengeDao.insertWeeks(weeks)
        }
    }

    suspend fun resetAllDataToZero() {
        transactionDao.clearAllTransactions()
        challengeDao.resetChallengeWeeks()
        val all = userDao.getAllUsersSync()
        for (u in all) {
            userDao.updateUser(u.copy(currentStreak = 0, monthlyWins = 0))
        }
        val group = groupDao.getGroupInfoSync()
        if (group != null) {
            groupDao.insertOrUpdateGroup(group.copy(isCompleted = false))
        }
        activityLogDao.clearLogs()
        activityLogDao.insertLog(
            ActivityLog(
                actorName = "Система",
                actionType = "RESET",
                details = "Ҳамаи маблағҳо ва сабтҳо тоза карда шуда, пасандоз аз 0 TJS аз нав сар шуд."
            )
        )
    }

    suspend fun addTransaction(
        user: User,
        amount: Double,
        category: String,
        description: String,
        dateString: String
    ): Long {
        val now = System.currentTimeMillis()
        val tx = SavingsTransaction(
            userId = user.id,
            userName = user.name,
            userAvatar = user.avatarValue,
            amount = amount,
            category = category,
            description = description,
            timestamp = now,
            dateString = dateString.ifEmpty {
                SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(now))
            }
        )
        val id = transactionDao.insertTransaction(tx)
        syncTransactionToFirestore(tx.copy(id = id))

        // Log anti-cheat activity
        activityLogDao.insertLog(
            ActivityLog(
                actorName = user.name,
                actionType = "ADD",
                details = "${user.name} added +${amount.toInt()} TJS ($category)"
            )
        )

        // Update user streak & stats
        val updatedUser = user.copy(
            currentStreak = user.currentStreak + 1,
            maxStreak = maxOf(user.maxStreak, user.currentStreak + 1)
        )
        userDao.updateUser(updatedUser)

        checkAndTriggerGoalCompletion()

        return id
    }

    suspend fun editTransaction(
        transaction: SavingsTransaction,
        newAmount: Double,
        newCategory: String,
        newDescription: String,
        editorUser: User
    ) {
        val original = transaction.amount
        val updated = transaction.copy(
            amount = newAmount,
            category = newCategory,
            description = newDescription,
            isEdited = true,
            editedBy = editorUser.name,
            editedAt = System.currentTimeMillis(),
            originalAmount = original
        )
        transactionDao.updateTransaction(updated)

        activityLogDao.insertLog(
            ActivityLog(
                actorName = editorUser.name,
                actionType = "EDIT",
                details = "${editorUser.name} edited +${original.toInt()} → +${newAmount.toInt()} TJS ($newCategory)"
            )
        )

        checkAndTriggerGoalCompletion()
    }

    suspend fun deleteTransaction(transaction: SavingsTransaction, user: User) {
        transactionDao.deleteTransaction(transaction)

        activityLogDao.insertLog(
            ActivityLog(
                actorName = user.name,
                actionType = "DELETE",
                details = "${user.name} deleted a transaction of ${transaction.amount.toInt()} TJS (${transaction.category})"
            )
        )

        checkAndTriggerGoalCompletion()
    }

    private suspend fun checkAndTriggerGoalCompletion() {
        val currentGroup = groupDao.getGroupInfoSync() ?: return
        val allTx = transactionDao.getAllTransactions().first()
        val total = allTx.sumOf { it.amount }
        if (total >= currentGroup.targetAmount && !currentGroup.isCompleted) {
            groupDao.setCompletedMode(currentGroup.groupCode, true)
            activityLogDao.insertLog(
                ActivityLog(
                    actorName = "SYSTEM",
                    actionType = "SETTING",
                    details = "🎉 GOAL REACHED! 30,000 TJS collected! Completed mode activated."
                )
            )
        }
    }

    suspend fun setCompletedModeManually(completed: Boolean) {
        val group = groupDao.getGroupInfoSync() ?: return
        groupDao.setCompletedMode(group.groupCode, completed)
        activityLogDao.insertLog(
            ActivityLog(
                actorName = "ADMIN",
                actionType = "SETTING",
                details = "Admin switched mode to: ${if (completed) "COMPLETED MODE 🏆" else "SAVING MODE 🟢"}"
            )
        )
    }

    suspend fun setStreakCalculation(mode: String) {
        val group = groupDao.getGroupInfoSync() ?: return
        groupDao.setStreakMode(group.groupCode, mode)
        activityLogDao.insertLog(
            ActivityLog(
                actorName = "ADMIN",
                actionType = "SETTING",
                details = "Admin changed streak calculation to $mode"
            )
        )
    }

    suspend fun updateUserProfile(user: User) {
        userDao.updateUser(user)
    }

    suspend fun updateUserCredentials(userId: String, newPassword: String, newPin: String) {
        val user = userDao.getUserById(userId) ?: return
        val updated = user.copy(
            passwordHash = if (newPassword.isNotBlank()) newPassword else user.passwordHash,
            pinCode = if (newPin.isNotBlank()) newPin else user.pinCode
        )
        userDao.updateUser(updated)
        activityLogDao.insertLog(
            ActivityLog(
                actorName = user.name,
                actionType = "SETTING",
                details = "${user.name} changed security credentials (Password/PIN)"
            )
        )
    }

    suspend fun linkGoogleAccount(userId: String, googleEmail: String) {
        val user = userDao.getUserById(userId) ?: return
        val updated = user.copy(
            googleEmail = googleEmail,
            isGoogleLinked = true
        )
        userDao.updateUser(updated)
        activityLogDao.insertLog(
            ActivityLog(
                actorName = user.name,
                actionType = "SETTING",
                details = "${user.name} linked Google account: $googleEmail"
            )
        )
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
        syncUserToFirestore(user)
        activityLogDao.insertLog(
            ActivityLog(
                actorName = user.name,
                actionType = "JOIN",
                details = "${user.name} joined the group via code ${user.groupCode}"
            )
        )
    }

    private fun syncUserToFirestore(user: User) {
        try {
            firestore?.collection("users")?.document(user.id)?.set(
                mapOf(
                    "id" to user.id,
                    "name" to user.name,
                    "email" to user.email,
                    "googleEmail" to user.googleEmail,
                    "role" to user.role,
                    "personalGoal" to user.personalGoal,
                    "currentStreak" to user.currentStreak,
                    "groupCode" to user.groupCode
                ),
                SetOptions.merge()
            )
        } catch (_: Exception) {}
    }

    private fun syncTransactionToFirestore(tx: SavingsTransaction) {
        try {
            val docId = if (tx.id != 0L) tx.id.toString() else "tx_${System.currentTimeMillis()}"
            firestore?.collection("transactions")?.document(docId)?.set(
                mapOf(
                    "userId" to tx.userId,
                    "userName" to tx.userName,
                    "amount" to tx.amount,
                    "category" to tx.category,
                    "description" to tx.description,
                    "timestamp" to tx.timestamp,
                    "dateString" to tx.dateString
                ),
                SetOptions.merge()
            )
        } catch (_: Exception) {}
    }

    suspend fun updateTravelPlan(plan: TravelPlan) {
        travelPlanDao.insertOrUpdate(plan)
    }

    suspend fun updateChallengeWeek(week: ChallengeWeek) {
        challengeDao.updateWeek(week)
    }

    suspend fun exportDataJson(): String {
        val group = groupDao.getGroupInfoSync()
        val users = allUsers.first()
        val txs = allTransactions.first()
        val plan = travelPlan.first()

        val root = JSONObject()
        group?.let {
            val gObj = JSONObject().apply {
                put("groupCode", it.groupCode)
                put("groupName", it.groupName)
                put("targetAmount", it.targetAmount)
                put("currency", it.currency)
                put("streakCalculation", it.streakCalculation)
                put("isCompleted", it.isCompleted)
            }
            root.put("group", gObj)
        }

        val usersArr = JSONArray()
        users.forEach { u ->
            usersArr.put(JSONObject().apply {
                put("id", u.id)
                put("name", u.name)
                put("email", u.email)
                put("role", u.role)
                put("personalGoal", u.personalGoal)
                put("currentStreak", u.currentStreak)
            })
        }
        root.put("users", usersArr)

        val txArr = JSONArray()
        txs.forEach { t ->
            txArr.put(JSONObject().apply {
                put("id", t.id)
                put("userId", t.userId)
                put("userName", t.userName)
                put("amount", t.amount)
                put("category", t.category)
                put("description", t.description)
                put("timestamp", t.timestamp)
                put("isEdited", t.isEdited)
                put("editedBy", t.editedBy)
                put("originalAmount", t.originalAmount)
            })
        }
        root.put("transactions", txArr)

        plan?.let {
            root.put("travelPlan", JSONObject().apply {
                put("destination", it.destination)
                put("travelDate", it.travelDate)
                put("flightInfo", it.flightInfo)
                put("hotelInfo", it.hotelInfo)
                put("placesToVisit", it.placesToVisit)
                put("plannedBudget", it.plannedBudget)
            })
        }

        return root.toString(2)
    }

    suspend fun importDataJson(jsonStr: String): Boolean {
        return try {
            val root = JSONObject(jsonStr)
            if (root.has("transactions")) {
                val txArr = root.getJSONArray("transactions")
                for (i in 0 until txArr.length()) {
                    val item = txArr.getJSONObject(i)
                    val tx = SavingsTransaction(
                        userId = item.optString("userId", "mustafa"),
                        userName = item.optString("userName", "Mustafa"),
                        userAvatar = item.optString("userId", "mustafa"),
                        amount = item.optDouble("amount", 0.0),
                        category = item.optString("category", "Дигар"),
                        description = item.optString("description", ""),
                        timestamp = item.optLong("timestamp", System.currentTimeMillis()),
                        isEdited = item.optBoolean("isEdited", false),
                        editedBy = item.optString("editedBy", ""),
                        originalAmount = item.optDouble("originalAmount", 0.0)
                    )
                    transactionDao.insertTransaction(tx)
                }
            }
            activityLogDao.insertLog(
                ActivityLog(
                    actorName = "ADMIN",
                    actionType = "SETTING",
                    details = "Backup restored successfully from JSON!"
                )
            )
            true
        } catch (e: Exception) {
            false
        }
    }
}
