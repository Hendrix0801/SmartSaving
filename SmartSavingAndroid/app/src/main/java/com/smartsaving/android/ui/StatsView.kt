package com.smartsaving.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smartsaving.android.data.TransactionType
import com.smartsaving.android.ui.theme.ExpenseRed
import com.smartsaving.android.ui.theme.IncomeGreen
import com.smartsaving.android.util.DateFormatter
import com.smartsaving.android.viewmodel.BudgetViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsView(viewModel: BudgetViewModel) {
    val store by viewModel.store.collectAsState()
    var selectedMonth by remember { mutableStateOf(System.currentTimeMillis()) }
    
    val isCurrentMonth = remember(selectedMonth) {
        val calendar = Calendar.getInstance()
        val selectedCalendar = Calendar.getInstance().apply { timeInMillis = selectedMonth }
        calendar.get(Calendar.MONTH) == selectedCalendar.get(Calendar.MONTH) &&
        calendar.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR)
    }
    
    val monthTitle = remember(selectedMonth) {
        DateFormatter.formatMonthYear(selectedMonth)
    }
    
    val monthTransactions = remember(store, selectedMonth) {
        store?.transactions(forMonth = selectedMonth) ?: emptyList()
    }
    
    val monthIncome = remember(store, selectedMonth) {
        store?.totalIncome(forMonth = selectedMonth) ?: 0.0
    }
    
    val monthExpense = remember(store, selectedMonth) {
        store?.totalSpent(forMonth = selectedMonth) ?: 0.0
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("统计") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 月份选择器
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "选择月份",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        val availableMonths = store?.availableMonths ?: emptyList()
                        
                        if (availableMonths.isNotEmpty()) {
                            var expanded by remember { mutableStateOf(false) }
                            
                            // 确保初始选中当前月份或第一个可用月份
                            LaunchedEffect(availableMonths) {
                                val calendar = Calendar.getInstance()
                                val currentMonth = calendar.apply {
                                    set(Calendar.DAY_OF_MONTH, 1)
                                    set(Calendar.HOUR_OF_DAY, 0)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }.timeInMillis
                                
                                if (!availableMonths.contains(selectedMonth)) {
                                    selectedMonth = availableMonths.firstOrNull() ?: currentMonth
                                }
                            }
                            
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    value = monthTitle,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )
                                
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    availableMonths.forEach { month ->
                                        DropdownMenuItem(
                                            text = { Text(DateFormatter.formatMonthYear(month)) },
                                            onClick = {
                                                selectedMonth = month
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "暂无数据",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            // 月份概览
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = if (isCurrentMonth) "本月概览" else "$monthTitle 概览",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        store?.let { budgetStore ->
                            if (isCurrentMonth) {
                                StatRow("月薪", String.format("¥ %.2f", budgetStore.monthlySalary), androidx.compose.ui.graphics.Color(0xFFFF9800))
                                Spacer(modifier = Modifier.height(12.dp))
                                StatRow("已设定存钱目标", String.format("¥ %.2f", budgetStore.savingTarget), androidx.compose.ui.graphics.Color(0xFF2196F3))
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            
                            StatRow(
                                if (isCurrentMonth) "本月额外收入" else "收入",
                                String.format("¥ %.2f", monthIncome),
                                IncomeGreen
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            StatRow(
                                if (isCurrentMonth) "本月已花" else "支出",
                                String.format("¥ %.2f", monthExpense),
                                ExpenseRed
                            )
                        }
                    }
                }
            }

            // 动账列表
            item {
                Text(
                    text = if (isCurrentMonth) "最近动帐" else "$monthTitle 动帐",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (monthTransactions.isEmpty()) {
                item {
                    Text(
                        text = "暂无动帐记录",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(monthTransactions, key = { it.id }) { transaction ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = transaction.note ?: (if (transaction.type == TransactionType.INCOME) "收入" else "支出"),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = DateFormatter.formatTransactionDate(transaction.date),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Text(
                                text = if (transaction.type == TransactionType.INCOME) {
                                    String.format("+¥ %.2f", transaction.amount)
                                } else {
                                    String.format("-¥ %.2f", transaction.amount)
                                },
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (transaction.type == TransactionType.INCOME) IncomeGreen else ExpenseRed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )
    }
}
