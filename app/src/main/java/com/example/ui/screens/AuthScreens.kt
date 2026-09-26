package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.ui.AppLanguage
import com.example.ui.AppScreen
import com.example.ui.theme.*

@Composable
fun SplashScreen(
    onStart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .clickable(onClick = onStart)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text("🏔️ ✈️", fontSize = 64.sp)
            Text(
                text = "BANK OF TRAVEL",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
            Text(
                text = "Большие мечты начинаются с маленьких сбережений",
                color = TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF132B22))
                    .border(1.dp, MintNeon, RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Georgia 📍", color = MintNeon, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = MintNeon),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp)
            ) {
                Text("Кушодан (Войти)", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("🏔️", fontSize = 48.sp)
                Text(
                    text = "BANK OF TRAVEL",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
            }

            // Airplane Window Mockup
            Box(
                modifier = Modifier
                    .size(240.dp, 280.dp)
                    .clip(RoundedCornerShape(120.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1A385C), Color(0xFF0F1E33), Color(0xFF07121F))
                        )
                    )
                    .border(6.dp, DarkCardBorder, RoundedCornerShape(120.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("✈️", fontSize = 48.sp)
                    Text("🇬🇪 Тбилиси", color = MintNeon, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Добро пожаловать!",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Вместе мы достигнем нашей цели — Грузия!",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onGetStarted,
                    colors = ButtonDefaults.buttonColors(containerColor = MintNeon),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("welcome_start_button")
                ) {
                    Text("Начать", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Уже есть аккаунт? ", color = TextSecondary, fontSize = 13.sp)
                    Text(
                        text = "Войти",
                        color = MintNeon,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(onClick = onLoginClick)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    allUsers: List<User>,
    onLoginSuccess: (String) -> Unit,
    onNavigateRegister: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var emailOrUser by remember { mutableStateOf("mustafa@mail.com") }
    var password by remember { mutableStateOf("123456") }

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = { Text("Вход в аккаунт", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text("Войдите в личный аккаунт:", color = TextSecondary, fontSize = 14.sp)
            }

            item {
                OutlinedTextField(
                    value = emailOrUser,
                    onValueChange = { emailOrUser = it },
                    label = { Text("Email или имя пользователя") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "User", tint = MintNeon) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MintNeon,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedContainerColor = DarkCard,
                        unfocusedContainerColor = DarkCard
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Пароль") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Pass", tint = MintNeon) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MintNeon,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedContainerColor = DarkCard,
                        unfocusedContainerColor = DarkCard
                    )
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Запомнить меня", color = TextSecondary, fontSize = 12.sp)
                    Text(
                        text = "Забыли пароль?",
                        color = CyanNeon,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Истифодаи рамзи стандартӣ: 123456", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        val matched = allUsers.find {
                            it.email.equals(emailOrUser, ignoreCase = true) || it.name.equals(emailOrUser, ignoreCase = true)
                        }
                        if (matched != null) {
                            onLoginSuccess(matched.id)
                        } else {
                            onLoginSuccess("mustafa")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintNeon),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("login_submit_btn")
                ) {
                    Text("Войти", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Интихоби зуди иштирокчиён (Fast Login):",
                    color = TextMuted,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allUsers.forEach { user ->
                        OutlinedButton(
                            onClick = { onLoginSuccess(user.id) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                            border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                        ) {
                            Text(user.name, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Нет аккаунта? ", color = TextSecondary, fontSize = 13.sp)
                    Text(
                        text = "Зарегистрироваться",
                        color = MintNeon,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(onClick = onNavigateRegister)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: (name: String, email: String, pass: String, code: String) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var groupCode by remember { mutableStateOf("BOT-GEO-7K29") }

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = { Text("Создать аккаунт", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text("Барои ҳамроҳ шудан ба гурӯҳи сафар сабти ном шавед:", color = TextSecondary, fontSize = 13.sp)
            }

            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Номи Шумо / Имя") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MintNeon,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedContainerColor = DarkCard,
                        unfocusedContainerColor = DarkCard
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MintNeon,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedContainerColor = DarkCard,
                        unfocusedContainerColor = DarkCard
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Пароль") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MintNeon,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedContainerColor = DarkCard,
                        unfocusedContainerColor = DarkCard
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = groupCode,
                    onValueChange = { groupCode = it },
                    label = { Text("Коди даъват ба гурӯҳ (Group Code)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MintNeon,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedContainerColor = DarkCard,
                        unfocusedContainerColor = DarkCard
                    )
                )
            }

            item {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            onRegisterSuccess(name, email, password, groupCode)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintNeon),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("register_submit_btn")
                ) {
                    Text("Зарегистрироваться", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    val alerts = listOf(
        Triple("Продолжай! Поездка в Грузию уже ближе! 🇬🇪", "2 мин назад", Icons.Default.Flight),
        Triple("Ты сегодня сохранил деньги. Отличная работа! 🔥", "1 час назад", Icons.Default.Whatshot),
        Triple("Ты сейчас #1 в Лидерборде! 🥇", "3 часа назад", Icons.Default.EmojiEvents),
        Triple("Осталось всего 11,550 TJS до достижения общей цели!", "6 часов назад", Icons.Default.Savings),
        Triple("Ayub исправил транзакцию +300 → +250 TJS", "1 день назад", Icons.Default.Edit)
    )

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = { Text("Огоҳиномаҳо (Уведомления)", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(alerts) { (msg, time, icon) ->
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
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF132B22)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = icon, contentDescription = "Alert", tint = MintNeon, modifier = Modifier.size(20.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = msg, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = time, color = TextMuted, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
