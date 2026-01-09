package com.smartsaving.android.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

class BudgetStore(
    var monthlySalary: Double = 0.0,
    var savingTarget: Double = 0.0,
    private val _transactions: MutableList<Transaction> = mutableListOf()
) {
    private val _transactionsFlow = MutableStateFlow(_transactions.toList())
    val transactionsFlow: StateFlow<List<Transaction>> = _transactionsFlow.asStateFlow()
    
    var transactions: List<Transaction>
        get() = _transactions.toList()
        set(value) {
            _transactions.clear()
            _transactions.addAll(value)
            _transactionsFlow.value = _transactions.toList()
        }

    // 本月支出总额
    val totalSpentThisMonth: Double
        get() {
            val calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)
            
            return transactions.filter { transaction ->
                val transCalendar = Calendar.getInstance().apply {
                    timeInMillis = transaction.date
                }
                transaction.type == TransactionType.EXPENSE &&
                transCalendar.get(Calendar.MONTH) == currentMonth &&
                transCalendar.get(Calendar.YEAR) == currentYear
            }.sumOf { it.amount }
        }

    // 本月收入总额
    val totalIncomeThisMonth: Double
        get() {
            val calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)
            
            return transactions.filter { transaction ->
                val transCalendar = Calendar.getInstance().apply {
                    timeInMillis = transaction.date
                }
                transaction.type == TransactionType.INCOME &&
                transCalendar.get(Calendar.MONTH) == currentMonth &&
                transCalendar.get(Calendar.YEAR) == currentYear
            }.sumOf { it.amount }
        }

    // 所有支出交易
    val expenseTransactions: List<Transaction>
        get() = transactions.filter { it.type == TransactionType.EXPENSE }

    // 所有收入交易
    val incomeTransactions: List<Transaction>
        get() = transactions.filter { it.type == TransactionType.INCOME }

    val daysRemainingInMonth: Int
        get() {
            val calendar = Calendar.getInstance()
            val today = calendar.get(Calendar.DAY_OF_MONTH)
            val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            // 剩余天数不包括今天，从明天开始计算
            return maxOf(1, lastDay - today)
        }

    val dailyAvailable: Double
        get() {
            // 可用资金 = 月薪 + 本月收入 - 存钱目标 - 本月支出
            val availablePool = monthlySalary + totalIncomeThisMonth - savingTarget - totalSpentThisMonth
            val days = maxOf(1, daysRemainingInMonth).toDouble()
            return maxOf(0.0, availablePool / days)
        }

    fun addTransaction(amount: Double, note: String?, type: TransactionType = TransactionType.EXPENSE) {
        val transaction = Transaction(
            amount = amount,
            note = note,
            type = type
        )
        _transactions.add(0, transaction)
        _transactionsFlow.value = _transactions.toList()
    }

    fun removeTransaction(transactionId: String) {
        _transactions.removeAll { it.id == transactionId }
        _transactionsFlow.value = _transactions.toList()
    }

    fun removeAllTransactions() {
        _transactions.clear()
        _transactionsFlow.value = _transactions.toList()
    }
}
