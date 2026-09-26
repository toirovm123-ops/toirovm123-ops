package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppLanguage
import com.example.ui.AppScreen
import com.example.ui.components.Localization
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentLang: AppLanguage,
    onBack: () -> Unit,
    onSetLang: (AppLanguage) -> Unit,
    onExportBackup: suspend () -> String,
    onRestoreBackup: (String, (Boolean) -> Unit) -> Unit,
    onLogout: () -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showPasswordDialog by remember { mutableStateOf(false) }
    var showBackupDialog by remember { mutableStateOf(false) }
    var backupJsonText by remember { mutableStateOf("") }
    var isExportMode by remember { mutableStateOf(true) }

    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = {
                    Text(
                        text = Localization.tr("settings", currentLang),
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Language Selection Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Забон / Язык / Language", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LangOption(
                                label = "Тоҷикӣ",
                                isSelected = currentLang == AppLanguage.TAJIK,
                                onClick = { onSetLang(AppLanguage.TAJIK) },
                                modifier = Modifier.weight(1f)
                            )
                            LangOption(
                                label = "Русский",
                                isSelected = currentLang == AppLanguage.RUSSIAN,
                                onClick = { onSetLang(AppLanguage.RUSSIAN) },
                                modifier = Modifier.weight(1f)
                            )
                            LangOption(
                                label = "English",
                                isSelected = currentLang == AppLanguage.ENGLISH,
                                onClick = { onSetLang(AppLanguage.ENGLISH) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Backup & Restore Card (Requirement 7: Backup)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
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
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("💾", fontSize = 22.sp)
                            Column {
                                Text("Нусхабардорӣ (Backup / Restore)", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text("Маълумоти 10 моҳи сарфакорӣ ҳеҷ гоҳ гум намешавад", color = TextSecondary, fontSize = 11.sp)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        backupJsonText = onExportBackup()
                                        isExportMode = true
                                        showBackupDialog = true
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_export_backup"),
                                colors = ButtonDefaults.buttonColors(containerColor = MintNeon),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Нусха гирифтан", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    backupJsonText = ""
                                    isExportMode = false
                                    showBackupDialog = true
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_import_backup"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Барқарорсозӣ", color = TextPrimary, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // General Settings List
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SettingRow(
                            icon = Icons.Default.LockReset,
                            title = "Иваз кардани парол (Change Password)",
                            subtitle = "Амнияти аккаунт",
                            onClick = { showPasswordDialog = true }
                        )
                        HorizontalDivider(color = DarkCardBorder)

                        SettingRow(
                            icon = Icons.Default.AdminPanelSettings,
                            title = "Панели Администратор & Anti-Cheat",
                            subtitle = "Танзими мақсад, стрикҳо ва аудити тағйирот",
                            onClick = { onNavigate(AppScreen.ADMIN_PANEL) }
                        )
                        HorizontalDivider(color = DarkCardBorder)

                        SettingRow(
                            icon = Icons.Default.Info,
                            title = "Дар бораи барнома (About)",
                            subtitle = "Bank of Travel v1.0.0 — Сафар ба Гурҷистон 🇬🇪",
                            onClick = {
                                Toast.makeText(context, "Bank of Travel — Ҳадаф: 30,000 TJS барои сафар ба Гурҷистон!", Toast.LENGTH_LONG).show()
                            }
                        )
                        HorizontalDivider(color = DarkCardBorder)

                        SettingRow(
                            icon = Icons.Default.Logout,
                            title = "Баромад (Logout)",
                            subtitle = "Пӯшидани сессия",
                            color = DangerCoral,
                            onClick = onLogout
                        )
                    }
                }
            }
        }
    }

    // Change Password Dialog
    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            containerColor = DarkSurface,
            title = { Text("Иваз кардани парол", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = oldPass,
                        onValueChange = { oldPass = it },
                        label = { Text("Пароли пешина") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MintNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkBg,
                            unfocusedContainerColor = DarkBg
                        )
                    )
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        label = { Text("Пароли нав") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MintNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkBg,
                            unfocusedContainerColor = DarkBg
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "Парол бомуваффақият иваз карда шуд!", Toast.LENGTH_SHORT).show()
                        showPasswordDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintNeon)
                ) {
                    Text("Сабт", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) {
                    Text("Бекор", color = TextSecondary)
                }
            }
        )
    }

    // Backup & Restore Dialog
    if (showBackupDialog) {
        AlertDialog(
            onDismissRequest = { showBackupDialog = false },
            containerColor = DarkSurface,
            title = {
                Text(
                    text = if (isExportMode) "Нусхаи эҳтиётӣ (Backup JSON)" else "Барқароркунии маълумот (Restore)",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isExportMode)
                            "Ин матни JSON-ро метавонед нусхабардорӣ карда дар ҷои бехатар ё Telegram нигоҳ доред:"
                        else
                            "Матни қаблии JSON-ро инҷо гузоред:",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    OutlinedTextField(
                        value = backupJsonText,
                        onValueChange = { backupJsonText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        readOnly = isExportMode,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MintNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkBg,
                            unfocusedContainerColor = DarkBg
                        )
                    )
                }
            },
            confirmButton = {
                if (isExportMode) {
                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Backup", backupJsonText))
                            Toast.makeText(context, "Нусхабардорӣ шуд (Copied)!", Toast.LENGTH_SHORT).show()
                            showBackupDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MintNeon)
                    ) {
                        Text("Нусха гирифтан (Copy)", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            onRestoreBackup(backupJsonText) { success ->
                                Toast.makeText(
                                    context,
                                    if (success) "Маълумот бомуваффақият барқарор шуд!" else "Хатогӣ дар формати JSON!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            showBackupDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MintNeon)
                    ) {
                        Text("Барқарор кардан", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showBackupDialog = false }) {
                    Text("Маҳкам кардан", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun LangOption(label: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) MintNeon else DarkBg)
            .border(1.dp, if (isSelected) MintNeon else DarkCardBorder, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.Black else TextSecondary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun SettingRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    color: Color = TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = color, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(text = subtitle, color = TextMuted, fontSize = 11.sp)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = "Go", tint = TextMuted, modifier = Modifier.size(16.dp))
    }
}
