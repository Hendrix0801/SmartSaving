package com.smartsaving.android.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
enum class TransactionType {
    EXPENSE,  // 支出
    INCOME    // 收入
}

@Serializable
data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val date: Long = System.currentTimeMillis(),
    val amount: Double,
    val note: String? = null,
    val type: TransactionType = TransactionType.EXPENSE
)
