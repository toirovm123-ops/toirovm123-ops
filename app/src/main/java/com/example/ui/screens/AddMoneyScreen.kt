package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.ui.AppLanguage
import com.example.ui.AppScreen
import com.example.ui.components.Localization
import com.example.ui.components.UserAvatarView
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMoneyScreen(
    currentUser: User?,
    allUsers: List<User>,
    isCompletedMode: Boolean,
    lang: AppLanguage,
    onAddMoney: (amount: Double, category: String, description: String, date: String) -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    var amountText by remember { mutableStateOf("100") }
    var selectedCategory by remember { mutableStateOf("Онлайн работа") }
    var descriptionText by remember { mutableStateOf("") }
    var dateText by remember {
        mutableStateOf(SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date()))
    }
    var selectedUserId by remember { mutableStateOf(currentUser?.id ?: "mustafa") }

    val categories = listOf(
        "Зарплата / Робота" to Icons.Default.Work,
        "Онлайн работа" to Icons.Default.Laptop,
        "Бизнес" to Icons.Default.Storefront,
        "Продажи" to Icons.Default.TrendingUp,
        "Фриланс" to Icons.Default.Brush,
        "Подарок" to Icons.Default.CardGiftcard,
        "Дигар / Другое" to Icons.Default.MoreHoriz
    )

    val quickAmounts = listOf("50", "100", "200", "500", "1000", "2000")

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = {
                    Text(
                        text = if (lang == AppLanguage.TAJIK) "Маблағ илова кардан" else "Добавить средства",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(AppScreen.HOME) }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isCompletedMode) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(GoldMedal, MintNeon)))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("🏆", fontSize = 48.sp)
                        Text(
                            text = if (lang == AppLanguage.TAJIK) "Ҳадаф пурра иҷро шуд!" else "Цель полностью достигнута!",
                            color = GoldMedal,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = if (lang == AppLanguage.TAJIK)
                                "Маблағи 30,000 TJS ҷамъ шуд. Илова кардани маблағ баста шудааст. Натиҷаҳо ва ҷоизаҳои ниҳоиро бинед!"
                            else
                                "30,000 TJS собрано. Добавление денег заблокировано. Посмотрите финальные итоги и награды!",
                            color = TextSecondary,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { onNavigate(AppScreen.CELEBRATION_RESULT) },
                            colors = ButtonDefaults.buttonColors(containerColor = MintNeon),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (lang == AppLanguage.TAJIK) "Дидани натиҷаҳо 🏆" else "Посмотреть итоги 🏆",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Large Amount Input Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("amount_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkCard),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkElevated)))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Маблағи пасандоз (TJS)",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                OutlinedTextField(
                                    value = amountText,
                                    onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 7) amountText = it },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MintNeon,
                                        textAlign = TextAlign.Center
                                    ),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MintNeon,
                                        unfocusedBorderColor = DarkCardBorder,
                                        focusedContainerColor = DarkBg,
                                        unfocusedContainerColor = DarkBg
                                    ),
                                    modifier = Modifier
                                        .width(220.dp)
                                        .testTag("amount_input")
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("TJS", color = MintNeon, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Quick Amount Pills
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(quickAmounts) { q ->
                                    val isSelected = amountText == q
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isSelected) MintNeon else DarkElevated)
                                            .clickable { amountText = q }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "+$q",
                                            color = if (isSelected) Color.Black else TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Source of Funds (Category selection chips)
                item {
                    Text(
                        text = Localization.tr("source", lang),
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        categories.forEach { (catName, icon) ->
                            val isSelected = selectedCategory == catName
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedCategory = catName }
                                    .testTag("category_$catName"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) DarkElevated else DarkCard
                                ),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = Brush.linearGradient(
                                        if (isSelected) listOf(MintNeon, DarkCardBorder) else listOf(DarkCardBorder, DarkCardBorder)
                                    )
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(if (isSelected) MintNeon.copy(alpha = 0.2f) else DarkBg),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = catName,
                                                tint = if (isSelected) MintNeon else TextSecondary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Text(
                                            text = catName,
                                            color = if (isSelected) TextPrimary else TextSecondary,
                                            fontSize = 14.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }

                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = MintNeon,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Description (Optional)
                item {
                    Text(
                        text = "${Localization.tr("desc", lang)} (ихтиёрӣ)",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = descriptionText,
                        onValueChange = { descriptionText = it },
                        placeholder = { Text("Масалан: Freelance payment ё мукофотпулӣ", color = TextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("desc_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MintNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkCard,
                            unfocusedContainerColor = DarkCard
                        )
                    )
                }

                // Date
                item {
                    Text(
                        text = Localization.tr("date", lang),
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = dateText,
                        onValueChange = { dateText = it },
                        leadingIcon = {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Date", tint = MintNeon)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("date_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MintNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkCard,
                            unfocusedContainerColor = DarkCard
                        )
                    )
                }

                // Participant (Who is saving)
                item {
                    Text(
                        text = "Иштирокчӣ (Кӣ пул мегузорад?)",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        allUsers.forEach { user ->
                            val isSelected = user.id == selectedUserId
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedUserId = user.id }
                                    .testTag("select_user_${user.id}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) DarkElevated else DarkCard
                                ),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = Brush.linearGradient(
                                        if (isSelected) listOf(MintNeon, DarkCardBorder) else listOf(DarkCardBorder, DarkCardBorder)
                                    )
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    UserAvatarView(name = user.name, size = 36, avatarId = user.avatarValue)
                                    Text(
                                        text = user.name,
                                        color = if (isSelected) MintNeon else TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Submit Button
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = {
                            val amount = amountText.toDoubleOrNull() ?: 100.0
                            if (amount > 0) {
                                onAddMoney(amount, selectedCategory, descriptionText, dateText)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("submit_add_money_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintNeon,
                            contentColor = Color(0xFF07261C)
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color(0xFF07261C))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Localization.tr("save_btn", lang),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
