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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.data.model.ActivityLog
import com.example.data.model.GroupInfo
import com.example.data.model.User
import com.example.ui.AppLanguage
import com.example.ui.AppScreen
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    groupInfo: GroupInfo?,
    allUsers: List<User>,
    activityLogs: List<ActivityLog>,
    lang: AppLanguage,
    onBack: () -> Unit,
    onSetCompletedMode: (Boolean) -> Unit,
    onSetStreakMode: (String) -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    val isCompleted = groupInfo?.isCompleted == true
    val currentStreakCalc = groupInfo?.streakCalculation ?: "DAILY"

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = {
                    Text(
                        text = "Панели Администратор (Anti-Cheat & Admin)",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
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
            // Mode Controller (Saving Mode vs Completed Mode)
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
                            text = "Ҳолати барнома (Режим приложения):",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onSetCompletedMode(false) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!isCompleted) MintNeon else DarkElevated
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "🟢 SAVING",
                                    color = if (!isCompleted) Color.Black else TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            Button(
                                onClick = { onSetCompletedMode(true) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCompleted) GoldMedal else DarkElevated
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "🏆 COMPLETED",
                                    color = if (isCompleted) Color.Black else TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Text(
                            text = if (isCompleted)
                                "🏆 Ҳолати анҷомёбӣ фаъол: Илова кардани пул баста аст, натиҷаҳои ниҳоӣ кушода."
                            else
                                "🟢 Ҳолати ҷамъкунӣ фаъол: Ҳама метавонанд пул илова кунанд ва рақобат кунанд.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Streak Mode Setting (Daily vs Weekly)
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
                            text = "Ҳисобкунии стрик (Streak calculation):",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onSetStreakMode("DAILY") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (currentStreakCalc == "DAILY") MintNeon.copy(alpha = 0.2f) else DarkBg,
                                    contentColor = if (currentStreakCalc == "DAILY") MintNeon else TextSecondary
                                )
                            ) {
                                Text("Ҳар рӯз (Daily)")
                            }

                            OutlinedButton(
                                onClick = { onSetStreakMode("WEEKLY") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (currentStreakCalc == "WEEKLY") MintNeon.copy(alpha = 0.2f) else DarkBg,
                                    contentColor = if (currentStreakCalc == "WEEKLY") MintNeon else TextSecondary
                                )
                            ) {
                                Text("Ҳар ҳафта (Weekly)")
                            }
                        }
                    }
                }
            }

            // Group Code Info & Invite (Requirement 2: Invite code BOT-GEO-7K29)
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Коди даъват ба гурӯҳ (Group Code)", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = groupInfo?.groupCode ?: "BOT-GEO-7K29",
                                color = MintNeon,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text("Дӯстон ин кодро ворид мекунанд", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Anti-Cheat Activity Log Section (Requirement 4)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🛡️ Таърихи тағйирот (Anti-cheat Log):",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("${activityLogs.size} сабт", color = TextSecondary, fontSize = 12.sp)
                }
            }

            items(activityLogs, key = { it.id }) { log ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("log_item_${log.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val iconColor = when (log.actionType) {
                            "ADD" -> MintNeon
                            "EDIT" -> WarningAmber
                            "DELETE" -> DangerCoral
                            "JOIN" -> CyanNeon
                            else -> GoldMedal
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(iconColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (log.actionType) {
                                    "ADD" -> Icons.Default.Add
                                    "EDIT" -> Icons.Default.Edit
                                    "DELETE" -> Icons.Default.Delete
                                    "JOIN" -> Icons.Default.PersonAdd
                                    else -> Icons.Default.Settings
                                },
                                contentDescription = log.actionType,
                                tint = iconColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = log.details,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            val dateStr = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(Date(log.timestamp))
                            Text(
                                text = dateStr,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
