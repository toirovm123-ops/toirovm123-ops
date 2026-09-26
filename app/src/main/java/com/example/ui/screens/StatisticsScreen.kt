package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SavingsTransaction
import com.example.ui.AppLanguage
import com.example.ui.AppScreen
import com.example.ui.UserSummary
import com.example.ui.components.Localization
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    summaries: List<UserSummary>,
    transactions: List<SavingsTransaction>,
    totalSaved: Double,
    lang: AppLanguage,
    onNavigate: (AppScreen) -> Unit
) {
    var selectedPeriod by remember { mutableStateOf("Все время") }
    val periods = listOf("Сегодня", "Неделя", "Месяц", "Все время")

    val participantColors = listOf(MintNeon, GoldMedal, CyanNeon, Color(0xFFE879F9))

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = {
                    Text(
                        text = Localization.tr("stats", lang),
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { onNavigate(AppScreen.CELEBRATION_RESULT) }) {
                        Icon(Icons.Default.Assessment, contentDescription = "Reports", tint = MintNeon)
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
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    items(periods) { p ->
                        val isSelected = selectedPeriod == p
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) MintNeon else DarkCard)
                                .border(1.dp, if (isSelected) MintNeon else DarkCardBorder, RoundedCornerShape(12.dp))
                                .clickable { selectedPeriod = p }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("stats_period_$p")
                        ) {
                            Text(
                                text = p,
                                color = if (isSelected) Color.Black else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            title = "1,250 TJS",
                            subtitle = "Всего сегодня",
                            modifier = Modifier.weight(1f),
                            color = MintNeon,
                            tag = "metric_today"
                        )
                        MetricCard(
                            title = "178 TJS",
                            subtitle = "Средний день",
                            modifier = Modifier.weight(1f),
                            color = CyanNeon,
                            tag = "metric_avg_day"
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            title = "1,120 TJS",
                            subtitle = "Средняя неделя",
                            modifier = Modifier.weight(1f),
                            color = GoldMedal,
                            tag = "metric_avg_week"
                        )
                        MetricCard(
                            title = "4,650 TJS",
                            subtitle = "Средний месяц",
                            modifier = Modifier.weight(1f),
                            color = TextPrimary,
                            tag = "metric_avg_month"
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("donut_chart_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkElevated)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Сбережения по участникам",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(140.dp)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val strokeWidth = 24.dp.toPx()
                                    var startAngle = -90f
                                    val total = if (totalSaved > 0) totalSaved else 1.0

                                    summaries.forEachIndexed { index, u ->
                                        val sweep = ((u.totalSaved / total) * 360f).toFloat()
                                        val color = participantColors.getOrElse(index) { MintNeon }
                                        drawArc(
                                            color = color,
                                            startAngle = startAngle,
                                            sweepAngle = sweep - 3f,
                                            useCenter = false,
                                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                        )
                                        startAngle += sweep
                                    }
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${totalSaved.toInt()}",
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text("TJS", color = MintNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text("Всего", color = TextSecondary, fontSize = 9.sp)
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                summaries.forEachIndexed { index, item ->
                                    val color = participantColors.getOrElse(index) { MintNeon }
                                    val percent = if (totalSaved > 0) (item.totalSaved / totalSaved * 100).toInt() else 0
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                        )
                                        Text(
                                            text = item.user.name,
                                            color = TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "${item.totalSaved.toInt()} ($percent%)",
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
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Манбаъҳои маблағ (Источники)",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        val categories = transactions
                            .groupBy { it.category }
                            .mapValues { entry -> entry.value.sumOf { it.amount } }
                            .toList()
                            .sortedByDescending { it.second }

                        categories.forEach { (cat, amount) ->
                            val pct = if (totalSaved > 0) (amount / totalSaved).toFloat() else 0f
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = cat, color = TextPrimary, fontSize = 12.sp)
                                    Text(
                                        text = "${amount.toInt()} TJS (${(pct * 100).toInt()}%)",
                                        color = MintNeon,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape)
                                        .background(DarkBg)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(fraction = pct)
                                            .fillMaxHeight()
                                            .clip(CircleShape)
                                            .background(Brush.horizontalGradient(listOf(CyanNeon, MintNeon)))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    color: Color = TextPrimary,
    tag: String
) {
    Card(
        modifier = modifier.testTag(tag),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = color,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
