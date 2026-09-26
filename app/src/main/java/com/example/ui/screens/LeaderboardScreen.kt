package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.data.model.SpecialAwards
import com.example.ui.AppLanguage
import com.example.ui.AppScreen
import com.example.ui.UserSummary
import com.example.ui.components.Localization
import com.example.ui.components.UserAvatarView
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    summaries: List<UserSummary>,
    groupInfo: GroupInfo?,
    totalSaved: Double,
    awards: SpecialAwards,
    lang: AppLanguage,
    onNavigate: (AppScreen) -> Unit
) {
    var selectedTab by remember { mutableStateOf("Группа") }
    val target = groupInfo?.targetAmount ?: 30000.0
    val progress = if (target > 0) (totalSaved / target * 100).toInt() else 0

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = {
                    Text(
                        text = Localization.tr("leaderboard", lang),
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { onNavigate(AppScreen.ACHIEVEMENTS) }) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = "Awards", tint = GoldMedal)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Segmented Control Tabs (Группа / Личный)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkCard)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedTab == "Группа") CyanNeon else Color.Transparent)
                                .clickable { selectedTab = "Группа" }
                                .padding(vertical = 8.dp)
                                .testTag("tab_group"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Гурӯҳӣ / Группа",
                                color = if (selectedTab == "Группа") Color.Black else TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedTab == "Личный") CyanNeon else Color.Transparent)
                                .clickable { selectedTab = "Личный" }
                                .padding(vertical = 8.dp)
                                .testTag("tab_personal"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Шахсӣ / Личный",
                                color = if (selectedTab == "Личный") Color.Black else TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Hall of Fame Highlight Strip
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(AppScreen.CELEBRATION_RESULT) }
                        .testTag("hall_of_fame_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF132A3B)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyanNeon, MintNeon)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("🌟", fontSize = 24.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Номҳои махсус барои натиҷаҳо",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "💰 Biggest Saver: ${awards.biggestSaverName} | 🔥 Streak: ${awards.streakMasterName}",
                                color = MintNeon,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Leaderboard Items
            items(summaries) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("leaderboard_card_${item.rank}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(
                            when (item.rank) {
                                1 -> listOf(GoldMedal, DarkCardBorder)
                                2 -> listOf(SilverMedal, DarkCardBorder)
                                else -> listOf(BronzeMedal, DarkCardBorder)
                            }
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Rank Circle (1, 2, 3)
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    when (item.rank) {
                                        1 -> GoldMedal
                                        2 -> SilverMedal
                                        else -> BronzeMedal
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${item.rank}",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp
                            )
                        }

                        // Avatar
                        UserAvatarView(name = item.user.name, size = 44, avatarId = item.user.avatarValue)

                        // Name & Progress
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
                                    Text("ADMIN", color = GoldMedal, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            val pct = if (selectedTab == "Группа") item.percentageOfGroup / 100f else item.percentageOfPersonal / 100f
                            val clampedPct = pct.coerceIn(0f, 1f)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape)
                                    .background(DarkBg)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction = clampedPct)
                                        .fillMaxHeight()
                                        .clip(CircleShape)
                                        .background(
                                            when (item.rank) {
                                                1 -> MintNeon
                                                2 -> CyanNeon
                                                else -> Color(0xFFE2B93B)
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
                                    text = if (selectedTab == "Группа")
                                        "${item.percentageOfGroup.toInt()}% аз ҳадафи гурӯҳ"
                                    else
                                        "${item.percentageOfPersonal.toInt()}% аз ҳадафи шахсӣ",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Whatshot, contentDescription = "Streak", tint = WarningAmber, modifier = Modifier.size(12.dp))
                                    Text(
                                        text = "${item.user.currentStreak} рӯз",
                                        color = WarningAmber,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        // Amount Saved
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${item.totalSaved.toInt()} TJS",
                                color = MintNeon,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = when (item.rank) {
                                    1 -> "🥇 Ҷои 1"
                                    2 -> "🥈 Ҷои 2"
                                    else -> "🥉 Ҷои 3"
                                },
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // General Statistics Footer
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("group_summary_stats_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkElevated)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Омори умумии гурӯҳ (Общая статистика)",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${totalSaved.toInt()} TJS",
                                    color = MintNeon,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(text = "Ҳамагӣ пасандоз", color = TextSecondary, fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "$progress%",
                                    color = CyanNeon,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(text = "Иҷроиши нақша", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
