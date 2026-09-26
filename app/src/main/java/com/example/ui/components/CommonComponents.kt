package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppLanguage
import com.example.ui.AppScreen
import com.example.ui.theme.*

object Localization {
    fun tr(key: String, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.TAJIK -> when (key) {
                "home" -> "Асосӣ"
                "add" -> "Илова"
                "stats" -> "Омор"
                "leaderboard" -> "Пешсафон"
                "profile" -> "Профил"
                "goal" -> "Ҳадафи умумӣ"
                "saved" -> "Ҷамъшуда"
                "remaining" -> "Боқимонда"
                "participants" -> "Иштирокчиён"
                "source" -> "Манбаи маблағ"
                "desc" -> "Тавсиф"
                "date" -> "Сана"
                "save_btn" -> "Илова кардан"
                "completed" -> "Ҳадаф иҷро шуд!"
                "share" -> "Натиҷаро мубодила кунед"
                "edit" -> "Ислоҳ"
                "delete" -> "Нобуд кардан"
                "anti_cheat" -> "Таърихи тағйирот (Anti-cheat)"
                "travel_plan" -> "Нақшаи сафар (Гурҷистон 🇬🇪)"
                "streak" -> "Рӯз дар стрик"
                "settings" -> "Танзимот"
                else -> key
            }
            AppLanguage.RUSSIAN -> when (key) {
                "home" -> "Главная"
                "add" -> "Добавить"
                "stats" -> "Статистика"
                "leaderboard" -> "Лидерборд"
                "profile" -> "Профиль"
                "goal" -> "Общая цель"
                "saved" -> "Сбережено"
                "remaining" -> "Осталось"
                "participants" -> "Участники"
                "source" -> "Источник средств"
                "desc" -> "Описание"
                "date" -> "Дата"
                "save_btn" -> "Добавить"
                "completed" -> "Цель достигнута!"
                "share" -> "Поделиться результатом"
                "edit" -> "Редактировать"
                "delete" -> "Удалить"
                "anti_cheat" -> "История активности (Античит)"
                "travel_plan" -> "План поездки (Грузия 🇬🇪)"
                "streak" -> "Дней в стрике"
                "settings" -> "Настройки"
                else -> key
            }
            AppLanguage.ENGLISH -> when (key) {
                "home" -> "Home"
                "add" -> "Add"
                "stats" -> "Statistics"
                "leaderboard" -> "Leaderboard"
                "profile" -> "Profile"
                "goal" -> "Total Goal"
                "saved" -> "Saved"
                "remaining" -> "Remaining"
                "participants" -> "Participants"
                "source" -> "Source of Funds"
                "desc" -> "Description"
                "date" -> "Date"
                "save_btn" -> "Add Funds"
                "completed" -> "Goal Completed!"
                "share" -> "Share Result"
                "edit" -> "Edit"
                "delete" -> "Delete"
                "anti_cheat" -> "Audit Trail (Anti-cheat)"
                "travel_plan" -> "Travel Plan (Georgia 🇬🇪)"
                "streak" -> "Day Streak"
                "settings" -> "Settings"
                else -> key
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankTopBar(
    title: String,
    lang: AppLanguage,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    showNotification: Boolean = true,
    onNotificationClick: () -> Unit = {},
    onAdminClick: (() -> Unit)? = null
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkBg,
            titleContentColor = TextPrimary,
            navigationIconContentColor = TextPrimary,
            actionIconContentColor = TextPrimary
        ),
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("nav_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (onAdminClick != null) {
                IconButton(onClick = onAdminClick, modifier = Modifier.testTag("nav_admin_button")) {
                    Icon(
                        imageVector = Icons.Filled.AdminPanelSettings,
                        contentDescription = "Admin",
                        tint = GoldMedal
                    )
                }
            }
            if (showNotification) {
                IconButton(onClick = onNotificationClick, modifier = Modifier.testTag("nav_notification_button")) {
                    BadgedBox(badge = {
                        Badge(containerColor = MintNeon) {
                            Text("3", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = TextPrimary
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun BankBottomNav(
    currentScreen: AppScreen,
    onSelectScreen: (AppScreen) -> Unit,
    lang: AppLanguage
) {
    Surface(
        color = DarkSurface,
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(listOf(DarkCardBorder, Color.Transparent)),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = Icons.Default.Home,
                label = Localization.tr("home", lang),
                isSelected = currentScreen == AppScreen.HOME,
                onClick = { onSelectScreen(AppScreen.HOME) },
                tag = "tab_home"
            )
            NavItem(
                icon = Icons.Default.AddCircle,
                label = Localization.tr("add", lang),
                isSelected = currentScreen == AppScreen.ADD_MONEY,
                onClick = { onSelectScreen(AppScreen.ADD_MONEY) },
                isHighlight = true,
                tag = "tab_add"
            )
            NavItem(
                icon = Icons.Default.BarChart,
                label = Localization.tr("stats", lang),
                isSelected = currentScreen == AppScreen.STATISTICS,
                onClick = { onSelectScreen(AppScreen.STATISTICS) },
                tag = "tab_stats"
            )
            NavItem(
                icon = Icons.Default.EmojiEvents,
                label = Localization.tr("leaderboard", lang),
                isSelected = currentScreen == AppScreen.LEADERBOARD,
                onClick = { onSelectScreen(AppScreen.LEADERBOARD) },
                tag = "tab_leaderboard"
            )
            NavItem(
                icon = Icons.Default.Person,
                label = Localization.tr("profile", lang),
                isSelected = currentScreen == AppScreen.PROFILE,
                onClick = { onSelectScreen(AppScreen.PROFILE) },
                tag = "tab_profile"
            )
        }
    }
}

@Composable
fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isHighlight: Boolean = false,
    tag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(tag)
    ) {
        if (isHighlight) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(MintNeon, CyanNeon)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color(0xFF062218),
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) MintNeon else TextMuted,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MintNeon else TextMuted
        )
    }
}

@Composable
fun UserAvatarView(
    name: String,
    size: Int = 44,
    badgeText: String? = null,
    avatarId: String = "mustafa",
    onClick: (() -> Unit)? = null
) {
    val bgGradient = when (avatarId.lowercase()) {
        "mustafa" -> listOf(Color(0xFF00E5A3), Color(0xFF00755E))
        "ayub" -> listOf(Color(0xFFFFB800), Color(0xFF996E00))
        "muhammad", "muhammadsharif" -> listOf(Color(0xFF00C9FF), Color(0xFF006B99))
        else -> listOf(Color(0xFF8B5CF6), Color(0xFF4C1D95))
    }

    Box(
        modifier = Modifier
            .size(size.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(Brush.linearGradient(bgGradient))
                .border(1.5.dp, DarkCardBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.take(2).uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = (size * 0.38).sp
            )
        }

        if (badgeText != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(DarkBg)
                    .border(0.5.dp, DarkCardBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 3.dp, vertical = 1.dp)
            ) {
                Text(
                    text = badgeText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (badgeText) {
                        "Gold" -> GoldMedal
                        "Silver" -> SilverMedal
                        "Bronze" -> BronzeMedal
                        else -> MintNeon
                    }
                )
            }
        }
    }
}
