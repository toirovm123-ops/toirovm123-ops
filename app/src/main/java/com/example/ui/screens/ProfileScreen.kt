package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.ui.AppLanguage
import com.example.ui.AppScreen
import com.example.ui.UserSummary
import com.example.ui.components.Localization
import com.example.ui.components.UserAvatarView
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentUser: User?,
    allUsers: List<User>,
    userSummary: UserSummary?,
    lang: AppLanguage,
    onNavigate: (AppScreen) -> Unit,
    onSwitchUser: (String) -> Unit,
    onUpdateUser: (String, String) -> Unit,
    onUpdateSecurity: (password: String, pin: String) -> Unit = { _, _ -> },
    onLinkGoogle: (String) -> Unit = {},
    onLogout: () -> Unit
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(currentUser?.name ?: "") }
    var selectedAvatarPreset by remember { mutableStateOf(currentUser?.avatarValue ?: "mustafa") }
    var showSwitchUserDialog by remember { mutableStateOf(false) }
    var showSecurityDialog by remember { mutableStateOf(false) }
    var showGoogleLinkDialog by remember { mutableStateOf(false) }

    var newPasswordInput by remember { mutableStateOf(currentUser?.passwordHash ?: "123456") }
    var newPinInput by remember { mutableStateOf(currentUser?.pinCode ?: "1234") }
    var googleEmailInput by remember { mutableStateOf(currentUser?.googleEmail?.ifEmpty { "toirovm123@gmail.com" } ?: "toirovm123@gmail.com") }

    val avatarPresets = listOf("mustafa", "ayub", "muhammad", "alex", "david")

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = {
                    Text(
                        text = Localization.tr("profile", lang),
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { onNavigate(AppScreen.SETTINGS) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextPrimary)
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
            // Profile Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkElevated)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            UserAvatarView(
                                name = currentUser?.name ?: "Mustafa",
                                size = 76,
                                avatarId = currentUser?.avatarValue ?: "mustafa"
                            )
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(MintNeon)
                                    .clickable {
                                        editName = currentUser?.name ?: ""
                                        selectedAvatarPreset = currentUser?.avatarValue ?: "mustafa"
                                        showEditProfileDialog = true
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Avatar", tint = Color.Black, modifier = Modifier.size(15.dp))
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = currentUser?.name ?: "Mustafa",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentUser?.email ?: "mustafa@mail.com",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        // Badge Tier (Silver, Gold, etc.)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkElevated)
                                .border(1.dp, SilverMedal.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("🛡️", fontSize = 12.sp)
                                Text(
                                    text = userSummary?.badgeTier ?: "Silver",
                                    color = SilverMedal,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // 3 Metrics: Total saved, personal progress, streak
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${userSummary?.totalSaved?.toInt() ?: 8500}",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("TJS сбережено", color = TextSecondary, fontSize = 11.sp)
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${userSummary?.percentageOfPersonal?.toInt() ?: 85}%",
                                    color = MintNeon,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Личный прогресс", color = TextSecondary, fontSize = 11.sp)
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${currentUser?.currentStreak ?: 7}",
                                    color = WarningAmber,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Дней в стрике 🔥", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // EXCLUSIVE: Georgian Boarding Pass Ticket (Корти парвоз ба Гурҷистон)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(AppScreen.TRAVEL_PLAN) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2231)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFF00E5A3), Color(0xFF00C9FF))
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("✈️", fontSize = 16.sp)
                                Text(
                                    text = "GEORGIA TRIP 2026",
                                    color = MintNeon,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x3300E5A3))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "VIP BOARDING PASS",
                                    color = MintNeon,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Flight Route
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("DYU", color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Black)
                                Text("Душанбе", color = TextSecondary, fontSize = 11.sp)
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("ПАРВОЗ", color = CyanNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("━━━━ ✈️ ━━━━", color = TextMuted, fontSize = 12.sp)
                                Text("Direct Flight", color = TextMuted, fontSize = 9.sp)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("TBS", color = MintNeon, fontSize = 24.sp, fontWeight = FontWeight.Black)
                                Text("Тбилиси 🇬🇪", color = TextSecondary, fontSize = 11.sp)
                            }
                        }

                        HorizontalDivider(color = Color(0xFF1E3A52))

                        // Details Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("МУСОФИР (PASSENGER)", color = TextMuted, fontSize = 9.sp)
                                Text(currentUser?.name?.uppercase() ?: "MUSTAFO", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("САНА (DATE)", color = TextMuted, fontSize = 9.sp)
                                Text("15 AUG 2026", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("ҶОЙ (SEAT)", color = TextMuted, fontSize = 9.sp)
                                Text("12A (VIP)", color = GoldMedal, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("ҲОЛАТ", color = TextMuted, fontSize = 9.sp)
                                Text("ТАЪМИН ✅", color = MintNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Barcode Representation
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(26.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF08141F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "||||| | |||| ||| ||||||| | ||||| |||| || |||||||| |||||",
                                color = Color(0xFF38688D),
                                letterSpacing = 3.sp,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Google Account Connection Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("G", color = Color(0xFF4285F4), fontSize = 20.sp, fontWeight = FontWeight.Black)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("Google аккаунт", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF133629))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Пайваст ✅", color = MintNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(
                                text = if (currentUser?.googleEmail.isNullOrBlank()) "toirovm123@gmail.com" else currentUser?.googleEmail ?: "",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }

                        OutlinedButton(
                            onClick = { showGoogleLinkDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MintNeon),
                            border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                        ) {
                            Text("Иваз", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Achievements Preview
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(AppScreen.ACHIEVEMENTS) }
                        .testTag("my_achievements_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Мои достижения", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("Все (8)", color = MintNeon, fontSize = 12.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            AchievementBadge("🥇", "First 100", true)
                            AchievementBadge("💰", "First 500", true)
                            AchievementBadge("🔥", "7 Day Streak", true)
                            AchievementBadge("💎", "Diamond", true)
                        }
                    }
                }
            }

            // Profile Actions List
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ProfileMenuItem(
                            icon = Icons.Default.Lock,
                            title = "Амнияти аккаунт (Парол ва PIN)",
                            subtitle = "Танзими рамзи 4-рақама ва парол",
                            onClick = {
                                newPasswordInput = currentUser?.passwordHash ?: "123456"
                                newPinInput = currentUser?.pinCode ?: "1234"
                                showSecurityDialog = true
                            },
                            tag = "menu_security"
                        )
                        HorizontalDivider(color = DarkCardBorder)

                        ProfileMenuItem(
                            icon = Icons.Default.SwitchAccount,
                            title = "Иваз кардани иштирокчӣ (Switch User)",
                            subtitle = "Мустафо, Аюб, Муҳаммадшариф",
                            onClick = { showSwitchUserDialog = true },
                            tag = "menu_switch_user"
                        )
                        HorizontalDivider(color = DarkCardBorder)

                        ProfileMenuItem(
                            icon = Icons.Default.ReceiptLong,
                            title = "История транзакций",
                            subtitle = "Тамоми пардохтҳои Шумо",
                            onClick = { onNavigate(AppScreen.TRANSACTIONS) },
                            tag = "menu_tx"
                        )
                        HorizontalDivider(color = DarkCardBorder)

                        ProfileMenuItem(
                            icon = Icons.Default.FlightTakeoff,
                            title = "План поездки в Грузию 🇬🇪",
                            subtitle = "Чиптаҳо, меҳмонхона ва ҷойҳои диданӣ",
                            onClick = { onNavigate(AppScreen.TRAVEL_PLAN) },
                            tag = "menu_travel"
                        )
                        HorizontalDivider(color = DarkCardBorder)

                        ProfileMenuItem(
                            icon = Icons.Default.Security,
                            title = "Таърихи тағйирот (Anti-cheat)",
                            subtitle = "Шаффофияти 100% дар байни дӯстон",
                            onClick = { onNavigate(AppScreen.ADMIN_PANEL) },
                            tag = "menu_anti_cheat"
                        )
                        HorizontalDivider(color = DarkCardBorder)

                        ProfileMenuItem(
                            icon = Icons.Default.Settings,
                            title = Localization.tr("settings", lang),
                            subtitle = "Забон, амният, нусхабардорӣ",
                            onClick = { onNavigate(AppScreen.SETTINGS) },
                            tag = "menu_settings"
                        )
                        HorizontalDivider(color = DarkCardBorder)

                        ProfileMenuItem(
                            icon = Icons.Default.Logout,
                            title = "Баромад аз аккаунт (Logout)",
                            subtitle = "Амнияти аккаунт",
                            onClick = onLogout,
                            color = DangerCoral,
                            tag = "menu_logout"
                        )
                    }
                }
            }
        }
    }

    // Security & PIN Dialog
    if (showSecurityDialog) {
        AlertDialog(
            onDismissRequest = { showSecurityDialog = false },
            containerColor = DarkSurface,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "Security", tint = MintNeon)
                    Text("Амнияти аккаунт", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Парол ва PIN-коди 4-рақамаи худро барои воридшавии зуд нав кунед:",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    OutlinedTextField(
                        value = newPasswordInput,
                        onValueChange = { newPasswordInput = it },
                        label = { Text("Пароли нав") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MintNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkBg,
                            unfocusedContainerColor = DarkBg
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newPinInput,
                        onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) newPinInput = it },
                        label = { Text("PIN-код (4 рақам)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkBg,
                            unfocusedContainerColor = DarkBg
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateSecurity(newPasswordInput, newPinInput)
                        showSecurityDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintNeon)
                ) {
                    Text("Сабт кардан", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSecurityDialog = false }) {
                    Text("Бекор", color = TextSecondary)
                }
            }
        )
    }

    // Google Link Dialog
    if (showGoogleLinkDialog) {
        AlertDialog(
            onDismissRequest = { showGoogleLinkDialog = false },
            containerColor = DarkSurface,
            title = {
                Text("Пайвасти Google аккаунт", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Почтаи Google Gmail-и худро ворид кунед:", color = TextSecondary, fontSize = 13.sp)
                    OutlinedTextField(
                        value = googleEmailInput,
                        onValueChange = { googleEmailInput = it },
                        label = { Text("Google Gmail") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MintNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkBg,
                            unfocusedContainerColor = DarkBg
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (googleEmailInput.isNotBlank()) {
                            onLinkGoogle(googleEmailInput.trim())
                        }
                        showGoogleLinkDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintNeon)
                ) {
                    Text("Пайваст кардан", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showGoogleLinkDialog = false }) {
                    Text("Бекор", color = TextSecondary)
                }
            }
        )
    }

    // Edit Name & Avatar Dialog
    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            containerColor = DarkSurface,
            title = {
                Text("Танзими профил (Акс ва Ном)", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Номи Шумо") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MintNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkBg,
                            unfocusedContainerColor = DarkBg
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Интихоби намуди акс / аватар:", color = TextSecondary, fontSize = 13.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        avatarPresets.forEach { preset ->
                            val isSel = selectedAvatarPreset == preset
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, if (isSel) MintNeon else Color.Transparent, CircleShape)
                                    .clickable { selectedAvatarPreset = preset },
                                contentAlignment = Alignment.Center
                            ) {
                                UserAvatarView(name = preset, size = 40, avatarId = preset)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editName.isNotBlank()) {
                            onUpdateUser(editName, selectedAvatarPreset)
                        }
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintNeon)
                ) {
                    Text("Сабт кардан", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Бекор", color = TextSecondary)
                }
            }
        )
    }

    // Switch User Dialog
    if (showSwitchUserDialog) {
        AlertDialog(
            onDismissRequest = { showSwitchUserDialog = false },
            containerColor = DarkSurface,
            title = {
                Text("Иваз кардани корбар", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    allUsers.forEach { user ->
                        val isCurrent = user.id == currentUser?.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSwitchUser(user.id)
                                    showSwitchUserDialog = false
                                },
                            colors = CardDefaults.cardColors(containerColor = if (isCurrent) DarkElevated else DarkCard),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.linearGradient(
                                    if (isCurrent) listOf(MintNeon, DarkCardBorder) else listOf(DarkCardBorder, DarkCardBorder)
                                )
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                UserAvatarView(name = user.name, size = 36, avatarId = user.avatarValue)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(user.name, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(if (user.role == "ADMIN") "Администратор" else "Иштирокчӣ", color = TextSecondary, fontSize = 11.sp)
                                }
                                if (isCurrent) {
                                    Icon(Icons.Default.Check, contentDescription = "Active", tint = MintNeon)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSwitchUserDialog = false }) {
                    Text("Маҳкам кардан", color = MintNeon)
                }
            }
        )
    }
}

@Composable
fun AchievementBadge(icon: String, title: String, isUnlocked: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(if (isUnlocked) Color(0xFF1B3026) else DarkBg)
                .border(1.dp, if (isUnlocked) MintNeon.copy(alpha = 0.5f) else DarkCardBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 20.sp)
        }
        Text(title, color = if (isUnlocked) TextPrimary else TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    color: Color = TextPrimary,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(22.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = color, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = TextMuted, fontSize = 11.sp)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = "Open", tint = TextMuted, modifier = Modifier.size(18.dp))
    }
}
