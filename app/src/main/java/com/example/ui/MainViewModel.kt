package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.AppRepository
import com.example.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen {
    SPLASH,
    WELCOME,
    LOGIN,
    REGISTER,
    HOME,
    ADD_MONEY,
    TRANSACTIONS,
    LEADERBOARD,
    STATISTICS,
    PROFILE,
    NOTIFICATIONS,
    TRAVEL_PLAN,
    CHALLENGE_40W,
    ACHIEVEMENTS,
    SETTINGS,
    ADMIN_PANEL,
    CELEBRATION_RESULT
}

enum class AppLanguage {
    TAJIK,
    RUSSIAN,
    ENGLISH
}

data class UserSummary(
    val user: User,
    val totalSaved: Double,
    val percentageOfGroup: Float,
    val percentageOfPersonal: Float,
    val rank: Int,
    val badgeTier: String,
    val transactionCount: Int
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = AppRepository(db, application)
    }

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.TAJIK)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    val allUsers: StateFlow<List<User>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val groupInfo: StateFlow<GroupInfo?> = repository.groupInfo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allTransactions: StateFlow<List<SavingsTransaction>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activityLogs: StateFlow<List<ActivityLog>> = repository.activityLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val travelPlan: StateFlow<TravelPlan?> = repository.travelPlan
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val challengeWeeks: StateFlow<List<ChallengeWeek>> = repository.challengeWeeks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentUserId = MutableStateFlow("mustafa")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    val currentUser: StateFlow<User?> = combine(allUsers, _currentUserId) { users, id ->
        users.find { it.id == id } ?: users.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val totalGroupSaved: StateFlow<Double> = allTransactions.map { txList ->
        txList.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 18450.0)

    val userSummaries: StateFlow<List<UserSummary>> = combine(allUsers, allTransactions, groupInfo) { users, txList, group ->
        val groupTarget = group?.targetAmount ?: 30000.0
        val sortedUsers = users.map { u ->
            val userTx = txList.filter { it.userId == u.id }
            val sum = userTx.sumOf { it.amount }
            val groupPct = if (groupTarget > 0) (sum / groupTarget * 100).toFloat() else 0f
            val personalPct = if (u.personalGoal > 0) (sum / u.personalGoal * 100).toFloat() else 0f
            Triple(u, sum, userTx.size to (groupPct to personalPct))
        }.sortedByDescending { it.second }

        sortedUsers.mapIndexed { index, (user, totalSaved, meta) ->
            val (txCount, pcts) = meta
            val (groupPct, personalPct) = pcts
            val badge = when (index) {
                0 -> "Gold"
                1 -> "Silver"
                else -> "Bronze"
            }
            UserSummary(
                user = user,
                totalSaved = totalSaved,
                percentageOfGroup = groupPct,
                percentageOfPersonal = personalPct,
                rank = index + 1,
                badgeTier = badge,
                transactionCount = txCount
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val specialAwards: StateFlow<SpecialAwards> = combine(userSummaries, allUsers) { summaries, _ ->
        val biggestSaver = summaries.maxByOrNull { it.totalSaved }
        val streakMaster = summaries.maxByOrNull { it.user.currentStreak }
        val monthlyChamp = summaries.maxByOrNull { it.user.monthlyWins }
        val mostActive = summaries.maxByOrNull { it.transactionCount }
        val lowestEffort = summaries.minByOrNull { it.totalSaved }

        SpecialAwards(
            biggestSaverName = biggestSaver?.user?.name ?: "Mustafa",
            biggestSaverAmount = biggestSaver?.totalSaved ?: 8500.0,
            streakMasterName = streakMaster?.user?.name ?: "Mustafa",
            streakMasterDays = streakMaster?.user?.currentStreak ?: 7,
            monthlyChampionName = monthlyChamp?.user?.name ?: "Mustafa",
            monthlyChampionWins = monthlyChamp?.user?.monthlyWins ?: 2,
            mostActiveName = mostActive?.user?.name ?: "Mustafa",
            mostActiveCount = mostActive?.transactionCount ?: 4,
            biggestProgressName = "Ayub",
            biggestProgressPercent = 45,
            highestEffortUser = biggestSaver?.user?.name ?: "Mustafa",
            lowestEffortUser = lowestEffort?.user?.name ?: "Muhammadsharif"
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SpecialAwards("Mustafa", 8500.0, "Mustafa", 7, "Mustafa", 2, "Mustafa", 4, "Ayub", 45, "Mustafa", "Muhammadsharif")
    )

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun switchUser(userId: String) {
        _currentUserId.value = userId
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun addMoney(amount: Double, category: String, description: String, date: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.addTransaction(user, amount, category, description, date)
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun editMoney(transaction: SavingsTransaction, newAmount: Double, newCategory: String, newDesc: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.editTransaction(transaction, newAmount, newCategory, newDesc, user)
        }
    }

    fun deleteTransaction(transaction: SavingsTransaction) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            repository.deleteTransaction(transaction, user)
        }
    }

    fun setCompletedMode(completed: Boolean) {
        viewModelScope.launch {
            repository.setCompletedModeManually(completed)
        }
    }

    fun setStreakMode(mode: String) {
        viewModelScope.launch {
            repository.setStreakCalculation(mode)
        }
    }

    fun updateUser(name: String, avatar: String) {
        viewModelScope.launch {
            val current = currentUser.value ?: return@launch
            val updated = current.copy(name = name, avatarValue = avatar)
            repository.updateUserProfile(updated)
        }
    }

    fun updateTravelPlan(plan: TravelPlan) {
        viewModelScope.launch {
            repository.updateTravelPlan(plan)
        }
    }

    fun toggleChallengeWeek(week: ChallengeWeek) {
        viewModelScope.launch {
            val updated = week.copy(
                isCompleted = !week.isCompleted,
                savedAmount = if (!week.isCompleted) week.targetAmount else 0.0
            )
            repository.updateChallengeWeek(updated)
        }
    }

    fun registerNewUser(name: String, email: String, pass: String, groupCode: String) {
        viewModelScope.launch {
            val id = name.lowercase().replace(" ", "_")
            val newUser = User(
                id = id,
                name = name,
                email = email,
                passwordHash = pass,
                role = "MEMBER",
                avatarType = "PRESET",
                avatarValue = id,
                personalGoal = 10000.0,
                groupCode = groupCode.ifEmpty { "BOT-GEO-7K29" },
                currentStreak = 1,
                maxStreak = 1,
                monthlyWins = 0
            )
            repository.insertUser(newUser)
            _currentUserId.value = newUser.id
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun shareResult(context: Context) {
        val group = groupInfo.value
        val total = totalGroupSaved.value
        val target = group?.targetAmount ?: 30000.0
        val summaries = userSummaries.value
        val awards = specialAwards.value

        val shareText = buildString {
            appendLine("🇬🇪 BANK OF TRAVEL — GEORGIA TRIP 2026")
            appendLine("🎉 Ҳадаф бо муваффақият иҷро шуд! / Goal Completed!")
            appendLine("💰 ${total.toInt()} / ${target.toInt()} TJS (${(total / target * 100).toInt()}%)")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("🏆 ҶОЙГОҲИ ДАСТА (LEADERBOARD):")
            summaries.forEach { u ->
                val medal = when (u.rank) {
                    1 -> "🥇"
                    2 -> "🥈"
                    3 -> "🥉"
                    else -> "🎖️"
                }
                appendLine("$medal #${u.rank} ${u.user.name}: ${u.totalSaved.toInt()} TJS (${u.percentageOfGroup.toInt()}%)")
            }
            appendLine("━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("🌟 ҶОИЗАҲОИ МАХСУС:")
            appendLine("💰 Biggest Saver: ${awards.biggestSaverName} (${awards.biggestSaverAmount.toInt()} TJS)")
            appendLine("🔥 Streak Master: ${awards.streakMasterName} (${awards.streakMasterDays} рӯз)")
            appendLine("🏆 Monthly Champion: ${awards.monthlyChampionName} (${awards.monthlyChampionWins} моҳ)")
            appendLine("⚡ Most Active: ${awards.mostActiveName} (${awards.mostActiveCount} транзаксия)")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("✈️ Сафар ба Гурҷистон наздик шуд! Биёед парвоз кунем!")
            appendLine("#BankOfTravel #Georgia2026 #DreamTrip")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Bank of Travel Results")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    suspend fun getBackupJson(): String = repository.exportDataJson()

    fun restoreBackup(json: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = repository.importDataJson(json)
            onComplete(res)
        }
    }
}
