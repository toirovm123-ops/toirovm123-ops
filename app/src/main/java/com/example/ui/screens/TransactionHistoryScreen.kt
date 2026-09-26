package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SavingsTransaction
import com.example.data.model.User
import com.example.ui.AppLanguage
import com.example.ui.components.Localization
import com.example.ui.components.UserAvatarView
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    transactions: List<SavingsTransaction>,
    currentUser: User?,
    allUsers: List<User>,
    lang: AppLanguage,
    onBack: () -> Unit,
    onEditTransaction: (tx: SavingsTransaction, newAmount: Double, newCategory: String, newDesc: String) -> Unit,
    onDeleteTransaction: (tx: SavingsTransaction) -> Unit
) {
    var selectedFilterTime by remember { mutableStateOf("Все") }
    var selectedUserFilter by remember { mutableStateOf<String?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }

    var editingTransaction by remember { mutableStateOf<SavingsTransaction?>(null) }
    var editAmountText by remember { mutableStateOf("") }
    var editCategoryText by remember { mutableStateOf("") }
    var editDescText by remember { mutableStateOf("") }

    var deletingTransaction by remember { mutableStateOf<SavingsTransaction?>(null) }

    val filteredTransactions = transactions.filter { tx ->
        val userMatch = selectedUserFilter == null || tx.userId == selectedUserFilter
        val catMatch = selectedCategoryFilter == null || tx.category == selectedCategoryFilter
        userMatch && catMatch
    }

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBg),
                title = {
                    Text(
                        text = if (lang == AppLanguage.TAJIK) "Таърихи транзаксияҳо" else "История транзакций",
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
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    val timeFilters = listOf("Все", "День", "Неделя", "Месяц")
                    items(timeFilters) { t ->
                        val isSelected = selectedFilterTime == t
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) MintNeon else DarkCard)
                                .border(1.dp, if (isSelected) MintNeon else DarkCardBorder, RoundedCornerShape(12.dp))
                                .clickable { selectedFilterTime = t }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("filter_time_$t")
                        ) {
                            Text(
                                text = t,
                                color = if (isSelected) Color.Black else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        val isAll = selectedUserFilter == null
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isAll) CyanNeon.copy(alpha = 0.2f) else DarkCard)
                                .border(1.dp, if (isAll) CyanNeon else DarkCardBorder, RoundedCornerShape(10.dp))
                                .clickable { selectedUserFilter = null }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Ҳама иштирокчиён",
                                color = if (isAll) CyanNeon else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    items(allUsers) { u ->
                        val isSelected = selectedUserFilter == u.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) CyanNeon.copy(alpha = 0.2f) else DarkCard)
                                .border(1.dp, if (isSelected) CyanNeon else DarkCardBorder, RoundedCornerShape(10.dp))
                                .clickable { selectedUserFilter = u.id }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("filter_user_${u.id}")
                        ) {
                            Text(
                                text = u.name,
                                color = if (isSelected) CyanNeon else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            items(filteredTransactions, key = { it.id }) { tx ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("tx_card_${tx.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(DarkCardBorder, DarkCardBorder)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            UserAvatarView(name = tx.userName, size = 42, avatarId = tx.userAvatar)

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = tx.userName,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = tx.category,
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                                if (tx.description.isNotEmpty()) {
                                    Text(
                                        text = tx.description,
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "+${tx.amount.toInt()} TJS",
                                    color = MintNeon,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = tx.dateString.ifEmpty { "24.09.2026" },
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Requirement 3: Edited audit transparency badge
                        if (tx.isEdited) {
                            val editDate = SimpleDateFormat("dd MMM, yyyy HH:mm", Locale.getDefault()).format(Date(tx.editedAt))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(WarningAmber.copy(alpha = 0.12f))
                                    .border(1.dp, WarningAmber.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edited",
                                        tint = WarningAmber,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "Ислоҳ шудааст: ${tx.editedBy} — $editDate (Пешина: ${tx.originalAmount.toInt()} TJS)",
                                        color = WarningAmber,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    editingTransaction = tx
                                    editAmountText = tx.amount.toInt().toString()
                                    editCategoryText = tx.category
                                    editDescText = tx.description
                                },
                                modifier = Modifier.testTag("edit_tx_btn_${tx.id}")
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(Localization.tr("edit", lang), color = TextSecondary, fontSize = 11.sp)
                            }

                            TextButton(
                                onClick = { deletingTransaction = tx },
                                modifier = Modifier.testTag("delete_tx_btn_${tx.id}")
                            ) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = DangerCoral, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(Localization.tr("delete", lang), color = DangerCoral, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            if (filteredTransactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ҳеҷ транзаксия ёфт нашуд", color = TextMuted, fontSize = 14.sp)
                    }
                }
            }
        }
    }

    if (editingTransaction != null) {
        val targetTx = editingTransaction!!
        AlertDialog(
            onDismissRequest = { editingTransaction = null },
            containerColor = DarkSurface,
            title = {
                Text("Ислоҳи транзаксия (Edit Transaction)", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Барои шаффофияти рақобат номи Шумо ҳамчун тағйирдиҳанда сабт мешавад.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    OutlinedTextField(
                        value = editAmountText,
                        onValueChange = { if (it.all { c -> c.isDigit() }) editAmountText = it },
                        label = { Text("Маблағи нав (TJS)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MintNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkBg,
                            unfocusedContainerColor = DarkBg
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editCategoryText,
                        onValueChange = { editCategoryText = it },
                        label = { Text("Манбаи маблағ") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MintNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedContainerColor = DarkBg,
                            unfocusedContainerColor = DarkBg
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editDescText,
                        onValueChange = { editDescText = it },
                        label = { Text("Тавсифи сабаби ислоҳ") },
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
                        val newAmount = editAmountText.toDoubleOrNull() ?: targetTx.amount
                        onEditTransaction(targetTx, newAmount, editCategoryText, editDescText)
                        editingTransaction = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintNeon)
                ) {
                    Text("Сабт кардан", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingTransaction = null }) {
                    Text("Бекор кардан", color = TextSecondary)
                }
            }
        )
    }

    if (deletingTransaction != null) {
        val targetTx = deletingTransaction!!
        AlertDialog(
            onDismissRequest = { deletingTransaction = null },
            containerColor = DarkSurface,
            title = {
                Text("Нест кардани транзаксия?", color = DangerCoral, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "Оё мехоҳед транзаксияи ${targetTx.userName} ба маблағи ${targetTx.amount.toInt()} TJS-ро нест кунед? Ин амал дар Activity Log сабт мешавад.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteTransaction(targetTx)
                        deletingTransaction = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerCoral)
                ) {
                    Text("Ҳа, нест шавад", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingTransaction = null }) {
                    Text("Бекор", color = TextSecondary)
                }
            }
        )
    }
}
