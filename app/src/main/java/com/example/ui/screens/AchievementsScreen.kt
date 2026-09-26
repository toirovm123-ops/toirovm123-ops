package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.ui.AppLanguage
import com.example.ui.theme.*

data class BadgeDefinition(
    val id: String,
    val icon: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    currentUser: User?,
    lang: AppLanguage,
    onBack: () -> Unit
) {
    val streak = currentUser?.currentStreak ?: 7
    val maxStreak = currentUser?.maxStreak ?: 10
    val monthlyWins = currentUser?.monthlyWins ?: 2

    val badges = listOf(
        BadgeDefinition("b1", "🥇", "First 100", "Аввалин 100 TJS пасандоз карда шуд", true),
        BadgeDefinition("b2", "💰", "First 500", "500 TJS бомуваффақият ҷамъ омад", true),
        BadgeDefinition("b3", "🔥", "7 Day Streak", "7 рӯзи пай дар пай пасандоз", streak >= 7),
        BadgeDefinition("b4", "🔥", "10 Day Streak", "10 рӯз пай дар пай гузаронидани пул", maxStreak >= 10),
        BadgeDefinition("b5", "🏆", "3x Champion", "3 бор пай дар пай ҷои якум дар моҳ", monthlyWins >= 2),
        BadgeDefinition("b6", "🔥", "30 Day Streak", "30 рӯз бе таваққуф дар стрик", maxStreak >= 30),
        BadgeDefinition("b7", "💎", "Diamond 10k", "10,000 TJS шахсӣ ҷамъ шуд", false),
        BadgeDefinition("b8", "👑", "Master 15k", "15,000 TJS саҳмгузорӣ", false),
        BadgeDefinition("b9", "🇬🇪", "Georgia Ready", "20,000 TJS омодагии комил барои сафар", false),
        BadgeDefinition("b10", "🎯", "Goal Smasher", "30,000 TJS ҳадафи умумӣ фатҳ шуд!", true)
    )

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = {
                    Text(
                        text = "Дастовардҳо (Достижения)",
                        color = TextPrimary,
                        fontSize = 18.sp,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Дастовардҳои фаъоли Шумо барои сафар ба Гурҷистон:",
                color = TextSecondary,
                fontSize = 13.sp
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(badges, key = { it.id }) { b ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("achievement_card_${b.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (b.isUnlocked) Color(0xFF132A22) else DarkCard
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.linearGradient(
                                if (b.isUnlocked) listOf(MintNeon, DarkCardBorder) else listOf(DarkCardBorder, DarkCardBorder)
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(if (b.isUnlocked) Color(0xFF1E3D30) else DarkBg)
                                    .border(1.5.dp, if (b.isUnlocked) MintNeon else DarkCardBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(b.icon, fontSize = 26.sp)
                            }

                            Text(
                                text = b.title,
                                color = if (b.isUnlocked) TextPrimary else TextSecondary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = b.description,
                                color = if (b.isUnlocked) TextSecondary else TextMuted,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                minLines = 2
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (b.isUnlocked) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Unlocked", tint = MintNeon, modifier = Modifier.size(14.dp))
                                    Text("Кушода шуд", color = MintNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                } else {
                                    Icon(Icons.Default.Lock, contentDescription = "Locked", tint = TextMuted, modifier = Modifier.size(14.dp))
                                    Text("Қулф", color = TextMuted, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
