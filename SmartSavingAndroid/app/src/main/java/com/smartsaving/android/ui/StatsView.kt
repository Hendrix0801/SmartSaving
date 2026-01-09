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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsView(viewModel: BudgetViewModel) {
    val store by viewModel.store.collectAsState()

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
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "本月概览",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        store?.let { budgetStore ->
                            StatRow("月薪", String.format("¥ %.2f", budgetStore.monthlySalary), androidx.compose.ui.graphics.Color(0xFFFF9800))
                            Spacer(modifier = Modifier.height(12.dp))
                            StatRow("本月额外收入", String.format("¥ %.2f", budgetStore.totalIncomeThisMonth), IncomeGreen)
                            Spacer(modifier = Modifier.height(12.dp))
                            StatRow("已设定存钱目标", String.format("¥ %.2f", budgetStore.savingTarget), androidx.compose.ui.graphics.Color(0xFF2196F3))
                            Spacer(modifier = Modifier.height(12.dp))
                            StatRow("本月已花", String.format("¥ %.2f", budgetStore.totalSpentThisMonth), ExpenseRed)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "最近动帐",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            val recentTransactions = store?.transactions?.take(20) ?: emptyList()

            if (recentTransactions.isEmpty()) {
                item {
                    Text(
                        text = "暂无动帐记录",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(recentTransactions, key = { it.id }) { transaction ->
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
