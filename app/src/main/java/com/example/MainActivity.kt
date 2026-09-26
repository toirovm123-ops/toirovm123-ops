package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.*
import com.example.ui.components.BankBottomNav
import com.example.ui.screens.*
import com.example.ui.theme.DarkBg
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                BankOfTravelApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BankOfTravelApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val groupInfo by viewModel.groupInfo.collectAsStateWithLifecycle()
    val totalSaved by viewModel.totalGroupSaved.collectAsStateWithLifecycle()
    val transactions by viewModel.allTransactions.collectAsStateWithLifecycle()
    val activityLogs by viewModel.activityLogs.collectAsStateWithLifecycle()
    val travelPlan by viewModel.travelPlan.collectAsStateWithLifecycle()
    val challengeWeeks by viewModel.challengeWeeks.collectAsStateWithLifecycle()
    val summaries by viewModel.userSummaries.collectAsStateWithLifecycle()
    val awards by viewModel.specialAwards.collectAsStateWithLifecycle()
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()

    val showBottomNav = currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.ADD_MONEY,
        AppScreen.STATISTICS,
        AppScreen.LEADERBOARD,
        AppScreen.PROFILE
    )

    // Handle Android system back button properly
    if (currentScreen != AppScreen.HOME && currentScreen != AppScreen.WELCOME) {
        BackHandler {
            viewModel.navigateTo(AppScreen.HOME)
        }
    }

    Scaffold(
        containerColor = DarkBg,
        bottomBar = {
            if (showBottomNav) {
                BankBottomNav(
                    currentScreen = currentScreen,
                    onSelectScreen = { viewModel.navigateTo(it) },
                    lang = language
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg)
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.SPLASH -> {
                    SplashScreen(
                        onStart = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.WELCOME -> {
                    WelcomeScreen(
                        onGetStarted = { viewModel.navigateTo(AppScreen.HOME) },
                        onLoginClick = { viewModel.navigateTo(AppScreen.LOGIN) }
                    )
                }
                AppScreen.LOGIN -> {
                    LoginScreen(
                        allUsers = allUsers,
                        onLoginSuccess = { userId ->
                            viewModel.switchUser(userId)
                            viewModel.navigateTo(AppScreen.HOME)
                        },
                        onNavigateRegister = { viewModel.navigateTo(AppScreen.REGISTER) },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.REGISTER -> {
                    RegisterScreen(
                        onRegisterSuccess = { name, email, pass, code ->
                            viewModel.registerNewUser(name, email, pass, code)
                        },
                        onBack = { viewModel.navigateTo(AppScreen.LOGIN) }
                    )
                }
                AppScreen.HOME -> {
                    HomeScreen(
                        currentUser = currentUser,
                        groupInfo = groupInfo,
                        totalSaved = totalSaved,
                        summaries = summaries,
                        lang = language,
                        onNavigate = { viewModel.navigateTo(it) },
                        onSwitchUser = { viewModel.switchUser(it) }
                    )
                }
                AppScreen.ADD_MONEY -> {
                    AddMoneyScreen(
                        currentUser = currentUser,
                        allUsers = allUsers,
                        isCompletedMode = groupInfo?.isCompleted == true || totalSaved >= (groupInfo?.targetAmount ?: 30000.0),
                        lang = language,
                        onAddMoney = { amount, category, desc, date ->
                            viewModel.addMoney(amount, category, desc, date)
                        },
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
                AppScreen.TRANSACTIONS -> {
                    TransactionHistoryScreen(
                        transactions = transactions,
                        currentUser = currentUser,
                        allUsers = allUsers,
                        lang = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) },
                        onEditTransaction = { tx, newAmount, newCat, newDesc ->
                            viewModel.editMoney(tx, newAmount, newCat, newDesc)
                        },
                        onDeleteTransaction = { tx ->
                            viewModel.deleteTransaction(tx)
                        }
                    )
                }
                AppScreen.LEADERBOARD -> {
                    LeaderboardScreen(
                        summaries = summaries,
                        groupInfo = groupInfo,
                        totalSaved = totalSaved,
                        awards = awards,
                        lang = language,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
                AppScreen.STATISTICS -> {
                    StatisticsScreen(
                        summaries = summaries,
                        transactions = transactions,
                        totalSaved = totalSaved,
                        lang = language,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
                AppScreen.PROFILE -> {
                    val currentSummary = summaries.find { it.user.id == currentUser?.id }
                    ProfileScreen(
                        currentUser = currentUser,
                        allUsers = allUsers,
                        userSummary = currentSummary,
                        lang = language,
                        onNavigate = { viewModel.navigateTo(it) },
                        onSwitchUser = { viewModel.switchUser(it) },
                        onUpdateUser = { name, avatar ->
                            viewModel.updateUser(name, avatar)
                        },
                        onLogout = { viewModel.navigateTo(AppScreen.LOGIN) }
                    )
                }
                AppScreen.NOTIFICATIONS -> {
                    NotificationsScreen(
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.TRAVEL_PLAN -> {
                    TravelPlanScreen(
                        travelPlan = travelPlan,
                        totalSaved = totalSaved,
                        lang = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) },
                        onSaveTravelPlan = { viewModel.updateTravelPlan(it) }
                    )
                }
                AppScreen.CHALLENGE_40W -> {
                    ChallengeScreen(
                        weeks = challengeWeeks,
                        lang = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) },
                        onToggleWeek = { viewModel.toggleChallengeWeek(it) }
                    )
                }
                AppScreen.ACHIEVEMENTS -> {
                    AchievementsScreen(
                        currentUser = currentUser,
                        lang = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
                AppScreen.SETTINGS -> {
                    SettingsScreen(
                        currentLang = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) },
                        onSetLang = { viewModel.setLanguage(it) },
                        onExportBackup = { viewModel.getBackupJson() },
                        onRestoreBackup = { json, cb -> viewModel.restoreBackup(json, cb) },
                        onLogout = { viewModel.navigateTo(AppScreen.LOGIN) },
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
                AppScreen.ADMIN_PANEL -> {
                    AdminScreen(
                        groupInfo = groupInfo,
                        allUsers = allUsers,
                        activityLogs = activityLogs,
                        lang = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) },
                        onSetCompletedMode = { viewModel.setCompletedMode(it) },
                        onSetStreakMode = { viewModel.setStreakMode(it) },
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
                AppScreen.CELEBRATION_RESULT -> {
                    CelebrationResultScreen(
                        groupInfo = groupInfo,
                        totalSaved = totalSaved,
                        summaries = summaries,
                        awards = awards,
                        lang = language,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) },
                        onShare = { ctx -> viewModel.shareResult(ctx) }
                    )
                }
            }
        }
    }
}
