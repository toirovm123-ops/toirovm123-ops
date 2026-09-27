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
import com.example.ui.GoogleAuthHelper
import com.example.ui.theme.*
import kotlinx.coroutines.launch

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
fun GoogleLogoIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, Color(0xFFE0E0E0), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "G",
            fontWeight = FontWeight.Black,
            fontSize = 15.sp,
            color = Color(0xFF4285F4)
        )
    }
}

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Войти через Google"
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("google_signin_btn"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF1F1F1F)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            GoogleLogoIcon()
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F1F1F)
            )
        }
    }
}

@Composable
fun GoogleAccountChooserDialog(
    onDismiss: () -> Unit,
    onSelectAccount: (email: String, name: String) -> Unit
) {
    var showCustomInput by remember { mutableStateOf(false) }
    var customEmail by remember { mutableStateOf("") }
    var customName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GoogleLogoIcon()
                Column {
                    Text("Вход с аккаунтом Google", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Интихоби аккаунт барои сабт", color = TextSecondary, fontSize = 12.sp)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (!showCustomInput) {
                    // Default user account from environment
                    GoogleAccountItem(
                        email = "toirovm123@gmail.com",
                        name = "Тоиров Мустафо",
                        letter = "Т",
                        onClick = { onSelectAccount("toirovm123@gmail.com", "Мустафо Тоиров") }
                    )

                    GoogleAccountItem(
                        email = "ayub@gmail.com",
                        name = "Аюб",
                        letter = "А",
                        onClick = { onSelectAccount("ayub@gmail.com", "Аюб") }
                    )

                    GoogleAccountItem(
                        email = "muhammad@gmail.com",
                        name = "Муҳаммадшариф",
                        letter = "М",
                        onClick = { onSelectAccount("muhammad@gmail.com", "Муҳаммадшариф") }
                    )

                    OutlinedButton(
                        onClick = { showCustomInput = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MintNeon),
                        border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("+ Ворид кардани дигар Gmail", fontSize = 12.sp)
                    }
                } else {
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text("Номи Шумо") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MintNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkBg,
                            unfocusedContainerColor = DarkBg
                        )
                    )

                    OutlinedTextField(
                        value = customEmail,
                        onValueChange = { customEmail = it },
                        label = { Text("Google Gmail") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MintNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkBg,
                            unfocusedContainerColor = DarkBg
                        )
                    )

                    Button(
                        onClick = {
                            if (customEmail.isNotBlank()) {
                                onSelectAccount(
                                    customEmail.trim(),
                                    customName.trim().ifEmpty { customEmail.substringBefore("@") }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MintNeon),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Ворид шудан", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Бекор кардан", color = TextSecondary)
            }
        }
    )
}

@Composable
fun GoogleAccountItem(
    email: String,
    name: String,
    letter: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkElevated)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF234567)),
                contentAlignment = Alignment.Center
            ) {
                Text(letter, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(name, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(email, color = TextMuted, fontSize = 12.sp)
            }
            Icon(Icons.Default.CheckCircle, contentDescription = "Active", tint = MintNeon, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onLoginClick: () -> Unit,
    onGoogleLogin: (email: String, name: String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleAuthHelper = remember { GoogleAuthHelper(context) }
    var showGoogleDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("🏔️", fontSize = 42.sp)
                Text(
                    text = "BANK OF TRAVEL",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Сафар ба Гурҷистон 🇬🇪 2026",
                    color = MintNeon,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Airplane Window Mockup
            Box(
                modifier = Modifier
                    .size(220.dp, 260.dp)
                    .clip(RoundedCornerShape(110.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1A385C), Color(0xFF0F1E33), Color(0xFF07121F))
                        )
                    )
                    .border(6.dp, DarkCardBorder, RoundedCornerShape(110.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("✈️", fontSize = 48.sp)
                    Text("Душанбе ➔ Тбилиси", color = MintNeon, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("30,000 TJS Ҳадаф", color = TextSecondary, fontSize = 11.sp)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GoogleSignInButton(
                    onClick = {
                        coroutineScope.launch {
                            val res = googleAuthHelper.signInWithGoogle()
                            val u = res.getOrNull()
                            if (u != null) {
                                onGoogleLogin(u.email, u.displayName)
                            } else {
                                showGoogleDialog = true
                            }
                        }
                    },
                    text = "Войти через Google"
                )

                Button(
                    onClick = onGetStarted,
                    colors = ButtonDefaults.buttonColors(containerColor = MintNeon),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("welcome_start_button")
                ) {
                    Text("Кушодани барнома (Начать)", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Аккаунт доред? ", color = TextSecondary, fontSize = 13.sp)
                    Text(
                        text = "Воридшавӣ бо парол / PIN",
                        color = MintNeon,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(onClick = onLoginClick)
                    )
                }
            }
        }
    }

    if (showGoogleDialog) {
        GoogleAccountChooserDialog(
            onDismiss = { showGoogleDialog = false },
            onSelectAccount = { email, name ->
                showGoogleDialog = false
                onGoogleLogin(email, name)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    allUsers: List<User>,
    onLoginSuccess: (String) -> Unit,
    onGoogleLogin: (email: String, name: String) -> Unit = { _, _ -> },
    onValidateCredentials: (input: String, pass: String, (Boolean, String) -> Unit) -> Unit = { _, _, cb -> cb(true, "") },
    onNavigateRegister: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleAuthHelper = remember { GoogleAuthHelper(context) }
    var emailOrUser by remember { mutableStateOf("mustafa") }
    var password by remember { mutableStateOf("123456") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showGoogleDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = { Text("Воридшавӣ ба барнома", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
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
                Spacer(modifier = Modifier.height(6.dp))
                GoogleSignInButton(
                    onClick = {
                        coroutineScope.launch {
                            val res = googleAuthHelper.signInWithGoogle()
                            val u = res.getOrNull()
                            if (u != null) {
                                onGoogleLogin(u.email, u.displayName)
                            } else {
                                showGoogleDialog = true
                            }
                        }
                    },
                    text = "Войти через Google"
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DarkCardBorder)
                    Text("ё бо ном ва парол", color = TextMuted, fontSize = 12.sp)
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DarkCardBorder)
                }
            }

            item {
                OutlinedTextField(
                    value = emailOrUser,
                    onValueChange = {
                        emailOrUser = it
                        errorMessage = null
                    },
                    label = { Text("Ном ё Email") },
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
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Пароль ё PIN-код (4 рақам)") },
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

            if (errorMessage != null) {
                item {
                    Text(
                        text = errorMessage ?: "",
                        color = DangerCoral,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Стандартӣ: 123456 ё PIN 1234", color = TextMuted, fontSize = 11.sp)
                    Text(
                        text = "Кумаки рамз",
                        color = CyanNeon,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Пароли муқаррарӣ: 123456 ё PIN: 1234", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        if (emailOrUser.isBlank()) {
                            errorMessage = "Лутфан ном ё email-ро ворид кунед!"
                            return@Button
                        }
                        onValidateCredentials(emailOrUser, password) { success, msg ->
                            if (!success) {
                                errorMessage = msg
                            } else {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintNeon),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("login_submit_btn")
                ) {
                    Text("Войти (Воридшавӣ)", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Интихоби фаврии корбар (Fast Login):",
                    color = TextMuted,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allUsers.forEach { user ->
                        OutlinedButton(
                            onClick = {
                                onLoginSuccess(user.id)
                                Toast.makeText(context, "Ворид шудед: ${user.name}", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                            border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                        ) {
                            Text(user.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Аккаунти нав? ", color = TextSecondary, fontSize = 13.sp)
                    Text(
                        text = "Сабти ном (Регистрация)",
                        color = MintNeon,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(onClick = onNavigateRegister)
                    )
                }
            }
        }
    }

    if (showGoogleDialog) {
        GoogleAccountChooserDialog(
            onDismiss = { showGoogleDialog = false },
            onSelectAccount = { email, name ->
                showGoogleDialog = false
                onGoogleLogin(email, name)
                Toast.makeText(context, "Воридшавӣ бо Google: $email", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: (name: String, email: String, pass: String, pin: String, code: String) -> Unit,
    onGoogleLogin: (email: String, name: String) -> Unit = { _, _ -> },
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleAuthHelper = remember { GoogleAuthHelper(context) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("1234") }
    var groupCode by remember { mutableStateOf("BOT-GEO-7K29") }
    var showGoogleDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = { Text("Эҷоди аккаунти нав", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
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
                Spacer(modifier = Modifier.height(4.dp))
                GoogleSignInButton(
                    onClick = {
                        coroutineScope.launch {
                            val res = googleAuthHelper.signInWithGoogle()
                            val u = res.getOrNull()
                            if (u != null) {
                                onGoogleLogin(u.email, u.displayName)
                            } else {
                                showGoogleDialog = true
                            }
                        }
                    },
                    text = "Сабти номи фаврӣ бо Google"
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DarkCardBorder)
                    Text("ё маълумоти худро нависед", color = TextMuted, fontSize = 12.sp)
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DarkCardBorder)
                }
            }

            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Номи Шумо / Имя *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name", tint = MintNeon) },
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
                    label = { Text("Почтаи электронӣ (Email)") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = MintNeon) },
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
                    label = { Text("Рамзи махфӣ (Пароль)") },
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
                OutlinedTextField(
                    value = pinCode,
                    onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) pinCode = it },
                    label = { Text("PIN-код барои даромадани тез (4 рақам)") },
                    leadingIcon = { Icon(Icons.Default.Pin, contentDescription = "PIN", tint = CyanNeon) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanNeon,
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
                    label = { Text("Коди гурӯҳи сафар (Group Code)") },
                    leadingIcon = { Icon(Icons.Default.Group, contentDescription = "Group", tint = MintNeon) },
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
                        if (name.isBlank()) {
                            Toast.makeText(context, "Лутфан номи худро ворид кунед!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        onRegisterSuccess(
                            name.trim(),
                            email.trim(),
                            password.ifBlank { "123456" },
                            pinCode.ifBlank { "1234" },
                            groupCode.ifBlank { "BOT-GEO-7K29" }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintNeon),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("register_submit_btn")
                ) {
                    Text("Сабти ном (Создать)", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showGoogleDialog) {
        GoogleAccountChooserDialog(
            onDismiss = { showGoogleDialog = false },
            onSelectAccount = { googleEmail, googleName ->
                showGoogleDialog = false
                onGoogleLogin(googleEmail, googleName)
                Toast.makeText(context, "Сабти ном бо Google: $googleEmail", Toast.LENGTH_SHORT).show()
            }
        )
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
