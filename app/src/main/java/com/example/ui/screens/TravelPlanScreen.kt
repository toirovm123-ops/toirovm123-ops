package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.TravelPlan
import com.example.ui.AppLanguage
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelPlanScreen(
    travelPlan: TravelPlan?,
    totalSaved: Double,
    lang: AppLanguage,
    onBack: () -> Unit,
    onSaveTravelPlan: (TravelPlan) -> Unit
) {
    val plan = travelPlan ?: TravelPlan()
    var isEditing by remember { mutableStateOf(false) }

    var travelDate by remember { mutableStateOf(plan.travelDate) }
    var hotelInfo by remember { mutableStateOf(plan.hotelInfo) }
    var flightInfo by remember { mutableStateOf(plan.flightInfo) }
    var placesToVisit by remember { mutableStateOf(plan.placesToVisit) }
    var notes by remember { mutableStateOf(plan.notes) }
    var budget by remember { mutableStateOf(plan.plannedBudget.toInt().toString()) }

    val progress = (totalSaved / plan.plannedBudget).toFloat().coerceIn(0f, 1f)

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = {
                    Text(
                        text = "Грузия 🇬🇪 (План поездки)",
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
                    IconButton(onClick = {
                        if (isEditing) {
                            onSaveTravelPlan(
                                plan.copy(
                                    travelDate = travelDate,
                                    hotelInfo = hotelInfo,
                                    flightInfo = flightInfo,
                                    placesToVisit = placesToVisit,
                                    notes = notes,
                                    plannedBudget = budget.toDoubleOrNull() ?: 30000.0
                                )
                            )
                        }
                        isEditing = !isEditing
                    }) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                            contentDescription = "Edit Plan",
                            tint = MintNeon
                        )
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
            // Countdown Hero Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("travel_countdown_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkElevated)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🇬🇪", fontSize = 28.sp)
                            Column {
                                Text("Грузия", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("То сафар / До поездки", color = TextSecondary, fontSize = 12.sp)
                            }
                        }

                        // Days and Months Countdown Cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(DarkElevated)
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("224", color = MintNeon, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                                    Text("Рӯз / Дней", color = TextSecondary, fontSize = 12.sp)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(DarkElevated)
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("7", color = CyanNeon, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                                    Text("Моҳ / Месяцев", color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                        }

                        // Progress Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(DarkBg)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = progress)
                                    .fillMaxHeight()
                                    .clip(CircleShape)
                                    .background(Brush.horizontalGradient(listOf(CyanNeon, MintNeon)))
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("${plan.plannedBudget.toInt()} TJS", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text("Ҳадафи умумӣ", color = TextSecondary, fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("${totalSaved.toInt()} TJS (${(progress * 100).toInt()}%)", color = MintNeon, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text("Ҷамъ шуд", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Travel Plan Sections: Dates, Hotel, Flight, Places, Notes
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
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text("Ҷузъиёти сафар (Детали поездки)", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                        // Dates
                        PlanField(
                            icon = Icons.Default.CalendarMonth,
                            title = "Санаи сафар / Даты",
                            value = travelDate,
                            isEditing = isEditing,
                            onValueChange = { travelDate = it }
                        )

                        // Flight
                        PlanField(
                            icon = Icons.Default.Flight,
                            title = "✈️ Парвоз / Flight",
                            value = flightInfo,
                            isEditing = isEditing,
                            onValueChange = { flightInfo = it }
                        )

                        // Hotel
                        PlanField(
                            icon = Icons.Default.Hotel,
                            title = "🏨 Меҳмонхона / Hotel",
                            value = hotelInfo,
                            isEditing = isEditing,
                            onValueChange = { hotelInfo = it }
                        )

                        // Places to visit
                        PlanField(
                            icon = Icons.Default.Place,
                            title = "📍 Ҷойҳои диданӣ / Places to visit",
                            value = placesToVisit,
                            isEditing = isEditing,
                            isMultiLine = true,
                            onValueChange = { placesToVisit = it }
                        )

                        // Planned Budget
                        PlanField(
                            icon = Icons.Default.Paid,
                            title = "💰 Буҷаи ба нақшагирифта / Budget (TJS)",
                            value = budget,
                            isEditing = isEditing,
                            onValueChange = { budget = it }
                        )

                        // Notes
                        PlanField(
                            icon = Icons.Default.Notes,
                            title = "📝 Қайдҳо / Notes",
                            value = notes,
                            isEditing = isEditing,
                            isMultiLine = true,
                            onValueChange = { notes = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlanField(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    isEditing: Boolean,
    isMultiLine: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = MintNeon, modifier = Modifier.size(18.dp))
            Text(text = title, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                minLines = if (isMultiLine) 3 else 1,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MintNeon,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedContainerColor = DarkBg,
                    unfocusedContainerColor = DarkBg
                )
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkBg)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(text = value, color = TextPrimary, fontSize = 13.sp)
            }
        }
    }
}
