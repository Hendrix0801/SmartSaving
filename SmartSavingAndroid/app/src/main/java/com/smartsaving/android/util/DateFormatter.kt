package com.smartsaving.android.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private val dateFormatter = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault())
    
    fun formatTransactionDate(timestamp: Long): String {
        return dateFormatter.format(Date(timestamp))
    }
}
