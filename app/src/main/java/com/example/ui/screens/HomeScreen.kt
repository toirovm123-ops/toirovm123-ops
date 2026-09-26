package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GroupInfo
import com.example.data.model.User
import com.example.ui.AppLanguage
import com.example.ui.AppScreen
import com.example.ui.UserSummary
import com.example.ui.components.Localization
import com.example.ui.components.UserAvatarView
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    currentUser: User?,
    groupInfo: GroupInfo?,
    totalSaved: Double,
    summaries: List<UserSummary>,
    lang: AppLanguage,
    onNavigate: (AppScreen) -> Unit,
    onSwitchUser: (String) -> Unit
) {
    val target = groupInfo?.targetAmount ?: 30000.0
    val remaining = maxOf(0.0, target - totalSaved)
    val progress = if (target > 0) (totalSaved / target).toFloat().coerceIn(0f, 1f) else 0f
    val isCompleted = groupInfo?.isCompleted == true || totalSaved >= target

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp, top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    UserAvatarView(
                        name = currentUser?.name ?: "Mustafa",
                        size = 46,
                        avatarId = currentUser?.avatarValue ?: "mustafa",
                        onClick = { onNavigate(AppScreen.PROFILE) }
                    )
                    Column {
                        Text(
                            text = when (lang) {
                                AppLanguage.TAJIK -> "Салом, ${currentUser?.name ?: "Mustafa"}! 👋"
                                AppLanguage.RUSSIAN -> "Привет, ${currentUser?.name ?: "Mustafa"}! 👋"
                                AppLanguage.ENGLISH -> "Hello, ${currentUser?.name ?: "Mustafa"}! 👋"
                            },
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Whatshot,
                                contentDescription = "Streak",
                                tint = WarningAmber,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "${currentUser?.currentStreak ?: 7} ${Localization.tr("streak", lang)}",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = { onNavigate(AppScreen.TRAVEL_PLAN) },
                        modifier = Modifier.testTag("home_travel_btn")
                    ) {
                        Text("🇬🇪", fontSize = 22.sp)
                    }
                    IconButton(
                        onClick = { onNavigate(AppScreen.NOTIFICATIONS) },
                        modifier = Modifier.testTag("home_notif_btn")
                    ) {
                        BadgedBox(badge = {
                            Badge(containerColor = MintNeon) {
                                Text("4", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Alerts",
                                tint = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // COMPLETED BANNER (If completed mode is active)
        if (isCompleted) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(AppScreen.CELEBRATION_RESULT) }
                        .testTag("completed_banner_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF132B22)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(MintNeon, GoldMedal)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("🏆", fontSize = 32.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = when (lang) {
                                    AppLanguage.TAJIK -> "ТАБРИК! ҲАДАФ ИҶРО ШУД!"
                                    AppLanguage.RUSSIAN -> "ПОЗДРАВЛЯЕМ! ЦЕЛЬ ДОСТИГНУТА!"
                                    AppLanguage.ENGLISH -> "CONGRATULATIONS! GOAL REACHED!"
                                },
                                color = MintNeon,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = when (lang) {
                                    AppLanguage.TAJIK -> "30,000 TJS ҷамъ шуд. Барои дидани натиҷаҳои ниҳоӣ клик кунед."
                                    AppLanguage.RUSSIAN -> "30,000 TJS собрано. Нажмите чтобы увидеть итоги."
                                    AppLanguage.ENGLISH -> "30,000 TJS collected. Click to view final rankings."
                                },
                                color = TextPrimary,
                                fontSize = 12.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Go",
                            tint = MintNeon
                        )
                    }
                }
            }
        }

        // Georgia Main Goal Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_goal_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkElevated)))
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Subtle background gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF1E3550).copy(alpha = 0.4f),
                                        Color(0xFF0F1E2E).copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("🇬🇪", fontSize = 24.sp)
                                Column {
                                    Text(
                                        text = groupInfo?.groupName ?: "Грузия — Наша цель",
                                        color = TextPrimary,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${target.toInt()} TJS ${Localization.tr("goal", lang)}",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            // Mode Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isCompleted) Color(0xFF1E3A2B) else Color(0xFF153326))
                                    .border(1.dp, if (isCompleted) GoldMedal else MintNeon, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isCompleted) "🏆 COMPLETED" else "🟢 SAVING MODE",
                                    color = if (isCompleted) GoldMedal else MintNeon,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Amounts Saved vs Remaining
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "${totalSaved.toInt()} TJS",
                                    color = MintNeon,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = Localization.tr("saved", lang),
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${remaining.toInt()} TJS",
                                    color = TextPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = Localization.tr("remaining", lang),
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Progress Bar with percentage
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(CircleShape)
                                    .background(DarkBg)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction = progress)
                                        .fillMaxHeight()
                                        .clip(CircleShape)
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(CyanNeon, MintNeon)
                                            )
                                        )
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Коди гурӯҳ: ${groupInfo?.groupCode ?: "BOT-GEO-7K29"}",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "${(progress * 100).toInt()}%",
                                    color = MintNeon,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2 Quick Stats Chips (5 месяцев осталось, 1,000 TJS в месяц)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigate(AppScreen.TRAVEL_PLAN) }
                        .testTag("card_months_left"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF132B3B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Months",
                                tint = CyanNeon,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text("5 моҳ / месяцев", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(Localization.tr("remaining", lang), color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigate(AppScreen.CHALLENGE_40W) }
                        .testTag("card_monthly_target"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF123428)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Savings,
                                contentDescription = "Monthly",
                                tint = MintNeon,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text("1,000 TJS", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Дар 1 моҳ", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Shortcut buttons for Challenge, Travel Plan, History
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { onNavigate(AppScreen.TRANSACTIONS) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_tx_history"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = "History", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Таърих", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { onNavigate(AppScreen.CHALLENGE_40W) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_challenge_40w"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Icon(imageVector = Icons.Default.Checklist, contentDescription = "Challenge", modifier = Modifier.size(16.dp), tint = MintNeon)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("40 ҳафта", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { onNavigate(AppScreen.TRAVEL_PLAN) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_travel_plan"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Text("🇬🇪 Сафар", fontSize = 12.sp)
                }
            }
        }

        // Participants section header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${Localization.tr("participants", lang)} (${summaries.size}/3)",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { onNavigate(AppScreen.LEADERBOARD) }) {
                    Text("Ҳамаро дидан", color = MintNeon, fontSize = 12.sp)
                }
            }
        }

        // Participants cards (Mustafa 8,500 TJS 85% Silver, Ayub 6,550 TJS 68% Gold, Muhammadsharif 3,400 TJS 34% Bronze)
        items(summaries) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSwitchUser(item.user.id) }
                    .testTag("participant_card_${item.user.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (item.user.id == currentUser?.id) DarkElevated else DarkCard
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(
                        if (item.user.id == currentUser?.id) listOf(MintNeon, DarkCardBorder) else listOf(DarkCardBorder, DarkCardBorder)
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    UserAvatarView(
                        name = item.user.name,
                        size = 46,
                        avatarId = item.user.avatarValue,
                        badgeText = item.badgeTier
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = item.user.name,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (item.user.role == "ADMIN") {
                                Text(
                                    text = "ADMIN",
                                    color = GoldMedal,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Mini progress bar for user's personal goal
                        val personalPct = (item.totalSaved / item.user.personalGoal).toFloat().coerceIn(0f, 1f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(DarkBg)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = personalPct)
                                    .fillMaxHeight()
                                    .clip(CircleShape)
                                    .background(
                                        when (item.badgeTier) {
                                            "Gold" -> GoldMedal
                                            "Silver" -> SilverMedal
                                            else -> BronzeMedal
                                        }
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${(personalPct * 100).toInt()}% мақсад",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "🔥 ${item.user.currentStreak} рӯз стрик",
                                color = WarningAmber,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${item.totalSaved.toInt()} TJS",
                            color = MintNeon,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${item.transactionCount} пардохт",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
