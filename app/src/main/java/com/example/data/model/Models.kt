package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String, // e.g. "mustafa", "ayub", "muhammadsharif"
    val name: String,
    val email: String,
    val passwordHash: String = "123456",
    val pinCode: String = "1234",
    val googleEmail: String = "",
    val isGoogleLinked: Boolean = false,
    val role: String = "MEMBER", // "ADMIN" or "MEMBER"
    val avatarType: String = "PRESET", // "PRESET" or "CUSTOM_URI"
    val avatarValue: String = "mustafa", // preset name or custom image uri
    val personalGoal: Double = 10000.0,
    val groupCode: String = "BOT-GEO-7K29",
    val currentStreak: Int = 7,
    val maxStreak: Int = 10,
    val monthlyWins: Int = 2
)

@Entity(tableName = "group_info")
data class GroupInfo(
    @PrimaryKey val groupCode: String = "BOT-GEO-7K29",
    val groupName: String = "Грузия — Наша цель 🇬🇪",
    val targetAmount: Double = 30000.0,
    val currency: String = "TJS",
    val streakCalculation: String = "DAILY", // "DAILY" or "WEEKLY"
    val isCompleted: Boolean = false,
    val targetMonths: Int = 5,
    val monthlyTarget: Double = 6000.0
)

@Entity(tableName = "transactions")
data class SavingsTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val userName: String,
    val userAvatar: String,
    val amount: Double,
    val category: String, // "Зарплата / Робота", "Онлайн работа", "Бизнес", "Продажи", "Фриланс", "Подарок", "Другое"
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String = "",
    val isEdited: Boolean = false,
    val editedBy: String = "",
    val editedAt: Long = 0L,
    val originalAmount: Double = 0.0
)

@Entity(tableName = "activity_logs")
data class ActivityLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val actorName: String,
    val actionType: String, // "ADD", "EDIT", "DELETE", "JOIN", "SETTING"
    val details: String
)

@Entity(tableName = "travel_plan")
data class TravelPlan(
    @PrimaryKey val id: Int = 1,
    val destination: String = "Грузия 🇬🇪 (Tbilisi & Batumi)",
    val travelDate: String = "15.08.2026 - 25.08.2026",
    val hotelInfo: String = "Tbilisi Old Town Hotel & Batumi Sea View Resort",
    val flightInfo: String = "Dushanbe ✈️ Tbilisi (Бохтар/Душанбе парвоз)",
    val placesToVisit: String = "1. Narikala Fortress\n2. Kazbegi (Степанцминда)\n3. Batumi Boulevard & Black Sea\n4. Kakheti виноделие\n5. Мцхета ва Джвари",
    val plannedBudget: Double = 30000.0,
    val notes: String = "Паспорт ва раводид санҷида шавад. Чемодан ва камера омода карда шавад!"
)

@Entity(tableName = "challenge_weeks")
data class ChallengeWeek(
    @PrimaryKey val weekNumber: Int,
    val targetAmount: Double = 750.0,
    val savedAmount: Double = 0.0,
    val isCompleted: Boolean = false
)

data class AchievementItem(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val targetValue: Double,
    val isUnlocked: Boolean,
    val progress: Float
)

data class SpecialAwards(
    val biggestSaverName: String,
    val biggestSaverAmount: Double,
    val streakMasterName: String,
    val streakMasterDays: Int,
    val monthlyChampionName: String,
    val monthlyChampionWins: Int,
    val mostActiveName: String,
    val mostActiveCount: Int,
    val biggestProgressName: String,
    val biggestProgressPercent: Int,
    val highestEffortUser: String,
    val lowestEffortUser: String
)
