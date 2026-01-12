package com.smartsaving.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.smartsaving.android.viewmodel.BudgetViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetView(viewModel: BudgetViewModel) {
    val store by viewModel.store.collectAsState()
    var salaryText by remember { mutableStateOf("") }
    var savingText by remember { mutableStateOf("") }
    var showClearConfirmation by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var saveJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }

    LaunchedEffect(store) {
        store?.let {
            if (it.monthlySalary > 0) {
                salaryText = String.format("%.0f", it.monthlySalary)
            }
            if (it.savingTarget > 0) {
                savingText = String.format("%.0f", it.savingTarget)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("预算") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "本月设置",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = salaryText,
                        onValueChange = { newValue ->
                            salaryText = newValue
                            saveJob?.cancel()
                            saveJob = scope.launch {
                                delay(500)
                                val value = newValue.toDoubleOrNull()
                                if (value != null && value > 0) {
                                    viewModel.updateMonthlySalary(value)
                                }
                            }
                        },
                        label = { Text("月薪") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = { Text("¥") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = savingText,
                        onValueChange = { newValue ->
                            savingText = newValue
                            saveJob?.cancel()
                            saveJob = scope.launch {
                                delay(500)
                                val value = newValue.toDoubleOrNull()
                                if (value != null && value >= 0) {
                                    viewModel.updateSavingTarget(value)
                                }
                            }
                        },
                        label = { Text("存钱目标") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = { Text("¥") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            store?.let { budgetStore ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "今日与本月概览",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "已花（本月）",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = String.format("¥ %.2f", budgetStore.totalSpentThisMonth),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "每日可用",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = String.format("¥ %.2f", budgetStore.dailyAvailable),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "剩余天数：${budgetStore.daysRemainingInMonth}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showClearConfirmation = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("清空本月记录")
                }
            }
        }
    }

    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            title = { Text("确认清空") },
            text = { Text("确定要清空所有交易记录吗？此操作无法撤销。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.removeAllTransactions()
                        showClearConfirmation = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("清空")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmation = false }) {
                    Text("取消")
                }
            }
        )
    }
}
