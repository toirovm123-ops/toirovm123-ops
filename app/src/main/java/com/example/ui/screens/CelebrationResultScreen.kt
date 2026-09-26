package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GroupInfo
import com.example.data.model.SpecialAwards
import com.example.ui.AppLanguage
import com.example.ui.UserSummary
import com.example.ui.components.UserAvatarView
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CelebrationResultScreen(
    groupInfo: GroupInfo?,
    totalSaved: Double,
    summaries: List<UserSummary>,
    awards: SpecialAwards,
    lang: AppLanguage,
    onBack: () -> Unit,
    onShare: (Context) -> Unit
) {
    val context = LocalContext.current
    val target = groupInfo?.targetAmount ?: 30000.0

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = {
                    Text(
                        text = "🏆 Итоги — Гурҷистон 2026",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { onShare(context) }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = MintNeon)
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Spectacular Fireworks / Celebration Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("celebration_hero_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF132822)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(MintNeon, GoldMedal)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("🇬🇪 🎉 ✈️", fontSize = 42.sp)

                        Text(
                            text = "ПОЗДРАВЛЯЕМ! ТАБРИК!",
                            color = GoldMedal,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Ҳадафи сафар ба Гурҷистон бомуваффақият иҷро шуд! Маблағи ${target.toInt()} TJS ҷамъ омад!",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0C1D18))
                                .border(1.dp, MintNeon, RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "💰 ${totalSaved.toInt()} / ${target.toInt()} TJS (100%)",
                                color = MintNeon,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Share Button
                        Button(
                            onClick = { onShare(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = MintNeon),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("share_result_btn")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Фиристодан ба Telegram / WhatsApp",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Effort Comparison Card (Most Effort vs Least Effort)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkElevated)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Таҳлили кӯшишҳо (Анализ вклада)",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Most effort
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF132B22)),
                                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(MintNeon, DarkCardBorder)))
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("🌟 Бештарин кӯшиш", color = MintNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text(awards.highestEffortUser, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Text("Ҳамагӣ саҳми калонтаринро гузошт ва ҳадафро пеш бурд!", color = TextSecondary, fontSize = 10.sp)
                                }
                            }

                            // Least effort
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF261D1D)),
                                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DangerCoral.copy(alpha = 0.5f), DarkCardBorder)))
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("📉 Камтарин кӯшиш", color = DangerCoral, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text(awards.lowestEffortUser, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Text("Ниёз ба ҳавасмандӣ дар сафари оянда барои баробарӣ!", color = TextSecondary, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Hall of Fame Special Titles (Номҳои махсус барои натиҷаҳо)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Ҷоизаҳои махсус (Special Nominations):",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        AwardRow("💰", "Biggest Saver", awards.biggestSaverName, "${awards.biggestSaverAmount.toInt()} TJS ҷамъ кард")
                        HorizontalDivider(color = DarkCardBorder)
                        AwardRow("🔥", "Streak Master", awards.streakMasterName, "${awards.streakMasterDays} рӯз пай дар пай стрик")
                        HorizontalDivider(color = DarkCardBorder)
                        AwardRow("🏆", "Monthly Champion", awards.monthlyChampionName, "${awards.monthlyChampionWins} бор ғолиби моҳ")
                        HorizontalDivider(color = DarkCardBorder)
                        AwardRow("⚡", "Most Active", awards.mostActiveName, "${awards.mostActiveCount} амалиёт гузаронид")
                        HorizontalDivider(color = DarkCardBorder)
                        AwardRow("🚀", "Biggest Progress", awards.biggestProgressName, "+${awards.biggestProgressPercent}% нисбат ба моҳи гузашта")
                    }
                }
            }

            // Final Table (Таблитсаи ниҳоӣ)
            item {
                Text(
                    text = "Таблитсаи натиҷаҳо (Итоговая таблица):",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(summaries) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = when (item.rank) {
                                1 -> "🥇"
                                2 -> "🥈"
                                else -> "🥉"
                            },
                            fontSize = 24.sp
                        )

                        UserAvatarView(name = item.user.name, size = 42, avatarId = item.user.avatarValue)

                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.user.name, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${item.percentageOfGroup.toInt()}% аз маблағи умумӣ",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${item.totalSaved.toInt()} TJS",
                                color = MintNeon,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${item.transactionCount} транзаксия",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AwardRow(icon: String, title: String, winnerName: String, reason: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(DarkBg),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 20.sp)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = GoldMedal, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = reason, color = TextSecondary, fontSize = 11.sp)
        }
        Text(text = winnerName, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
