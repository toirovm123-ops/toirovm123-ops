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

class AppRepository(private val db: AppDatabase, private val context: Context) {

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
                    name = "Mustafa",
                    email = "mustafa@mail.com",
                    passwordHash = "123456",
                    role = "ADMIN",
                    avatarType = "PRESET",
                    avatarValue = "mustafa",
                    personalGoal = 10000.0,
                    currentStreak = 7,
                    maxStreak = 10,
                    monthlyWins = 2
                ),
                User(
                    id = "ayub",
                    name = "Ayub",
                    email = "ayub@mail.com",
                    passwordHash = "123456",
                    role = "MEMBER",
                    avatarType = "PRESET",
                    avatarValue = "ayub",
                    personalGoal = 10000.0,
                    currentStreak = 5,
                    maxStreak = 8,
                    monthlyWins = 1
                ),
                User(
                    id = "muhammadsharif",
                    name = "Muhammadsharif",
                    email = "muhammad@mail.com",
                    passwordHash = "123456",
                    role = "MEMBER",
                    avatarType = "PRESET",
                    avatarValue = "muhammad",
                    personalGoal = 10000.0,
                    currentStreak = 3,
                    maxStreak = 5,
                    monthlyWins = 0
                )
            )
            userDao.insertUsers(users)

            val now = System.currentTimeMillis()
            val dayMs = 86400000L
            val df = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

            val txList = listOf(
                SavingsTransaction(
                    userId = "mustafa",
                    userName = "Mustafa",
                    userAvatar = "mustafa",
                    amount = 100.0,
                    category = "Онлайн работа",
                    description = "Freelance payment",
                    timestamp = now - dayMs * 2,
                    dateString = df.format(Date(now - dayMs * 2))
                ),
                SavingsTransaction(
                    userId = "ayub",
                    userName = "Ayub",
                    userAvatar = "ayub",
                    amount = 250.0,
                    category = "Зарплата / Робота",
                    description = "Monthly bonus deposit",
                    timestamp = now - dayMs * 3,
                    dateString = df.format(Date(now - dayMs * 3)),
                    isEdited = true,
                    editedBy = "Ayub",
                    editedAt = now - dayMs * 1,
                    originalAmount = 300.0
                ),
                SavingsTransaction(
                    userId = "muhammadsharif",
                    userName = "Muhammadsharif",
                    userAvatar = "muhammad",
                    amount = 150.0,
                    category = "Фриланс",
                    description = "Design logo project",
                    timestamp = now - dayMs * 4,
                    dateString = df.format(Date(now - dayMs * 4))
                ),
                SavingsTransaction(
                    userId = "mustafa",
                    userName = "Mustafa",
                    userAvatar = "mustafa",
                    amount = 300.0,
                    category = "Бизнес",
                    description = "E-commerce profit",
                    timestamp = now - dayMs * 5,
                    dateString = df.format(Date(now - dayMs * 5))
                ),
                SavingsTransaction(
                    userId = "ayub",
                    userName = "Ayub",
                    userAvatar = "ayub",
                    amount = 200.0,
                    category = "Продажи",
                    description = "Secondhand gear sale",
                    timestamp = now - dayMs * 6,
                    dateString = df.format(Date(now - dayMs * 6))
                ),
                SavingsTransaction(
                    userId = "mustafa",
                    userName = "Mustafa",
                    userAvatar = "mustafa",
                    amount = 8100.0,
                    category = "Зарплата / Робота",
                    description = "General savings pot",
                    timestamp = now - dayMs * 15,
                    dateString = df.format(Date(now - dayMs * 15))
                ),
                SavingsTransaction(
                    userId = "ayub",
                    userName = "Ayub",
                    userAvatar = "ayub",
                    amount = 6100.0,
                    category = "Бизнес",
                    description = "Major business deposit",
                    timestamp = now - dayMs * 18,
                    dateString = df.format(Date(now - dayMs * 18))
                ),
                SavingsTransaction(
                    userId = "muhammadsharif",
                    userName = "Muhammadsharif",
                    userAvatar = "muhammad",
                    amount = 3250.0,
                    category = "Онлайн работа",
                    description = "Contract deposit",
                    timestamp = now - dayMs * 20,
                    dateString = df.format(Date(now - dayMs * 20))
                )
            )
            for (tx in txList) {
                transactionDao.insertTransaction(tx)
            }

            val logs = listOf(
                ActivityLog(
                    timestamp = now - dayMs * 1,
                    actorName = "Ayub",
                    actionType = "EDIT",
                    details = "Ayub edited +300 → +250 TJS (Зарплата)"
                ),
                ActivityLog(
                    timestamp = now - dayMs * 2,
                    actorName = "Mustafa",
                    actionType = "ADD",
                    details = "Mustafa added +100 TJS (Онлайн работа)"
                ),
                ActivityLog(
                    timestamp = now - dayMs * 4,
                    actorName = "Muhammadsharif",
                    actionType = "ADD",
                    details = "Muhammadsharif added +150 TJS (Фриланс)"
                ),
                ActivityLog(
                    timestamp = now - dayMs * 25,
                    actorName = "Mustafa",
                    actionType = "SETTING",
                    details = "Mustafa created group 'BOT-GEO-7K29' with target 30,000 TJS"
                )
            )
            for (log in logs) {
                activityLogDao.insertLog(log)
            }

            travelPlanDao.insertOrUpdate(TravelPlan())

            val weeks = (1..40).map { w ->
                val isComp = w <= 24
                ChallengeWeek(
                    weekNumber = w,
                    targetAmount = 750.0,
                    savedAmount = if (isComp) 750.0 else if (w == 25) 450.0 else 0.0,
                    isCompleted = isComp
                )
            }
            challengeDao.insertWeeks(weeks)
        }
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

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
        activityLogDao.insertLog(
            ActivityLog(
                actorName = user.name,
                actionType = "JOIN",
                details = "${user.name} joined the group via code ${user.groupCode}"
            )
        )
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
