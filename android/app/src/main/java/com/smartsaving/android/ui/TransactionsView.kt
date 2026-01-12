package com.smartsaving.android.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.smartsaving.android.data.TransactionType
import com.smartsaving.android.ui.theme.ExpenseRed
import com.smartsaving.android.ui.theme.IncomeGreen
import com.smartsaving.android.viewmodel.BudgetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsView(viewModel: BudgetViewModel) {
    var transactionType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amountText by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }

    val isButtonDisabled = remember(amountText) {
        amountText.isEmpty() || amountText.toDoubleOrNull() == null || amountText.toDoubleOrNull()!! <= 0
    }

    val buttonText = if (transactionType == TransactionType.INCOME) "添加收入" else "添加支出"

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("记账") })
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
                        text = "新增动帐",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val incomeBackgroundAlpha = if (transactionType == TransactionType.INCOME) 0.15f else 0f
                        val expenseBackgroundAlpha = if (transactionType == TransactionType.EXPENSE) 0.15f else 0f

                        Button(
                            onClick = { transactionType = TransactionType.INCOME },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = IncomeGreen.copy(alpha = incomeBackgroundAlpha),
                                contentColor = IncomeGreen
                            )
                        ) {
                            Text(
                                "收入",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (transactionType == TransactionType.INCOME) 
                                        androidx.compose.ui.text.font.FontWeight.SemiBold 
                                    else androidx.compose.ui.text.font.FontWeight.Normal
                                )
                            )
                        }

                        Button(
                            onClick = { transactionType = TransactionType.EXPENSE },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ExpenseRed.copy(alpha = expenseBackgroundAlpha),
                                contentColor = ExpenseRed
                            )
                        ) {
                            Text(
                                "支出",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (transactionType == TransactionType.EXPENSE) 
                                        androidx.compose.ui.text.font.FontWeight.SemiBold 
                                    else androidx.compose.ui.text.font.FontWeight.Normal
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text("金额") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = { Text("¥") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        label = { Text("备注（可选）") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (amount != null && amount > 0) {
                        val note = noteText.trim().takeIf { it.isNotEmpty() }
                            ?: if (transactionType == TransactionType.INCOME) "收入" else "支出"
                        viewModel.addTransaction(amount, note, transactionType)
                        amountText = ""
                        noteText = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isButtonDisabled
            ) {
                Text(buttonText)
            }
        }
    }
}
