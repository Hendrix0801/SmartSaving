package com.smartsaving.android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartsaving.android.data.BudgetStore
import com.smartsaving.android.data.PersistenceManager
import com.smartsaving.android.data.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val persistenceManager = PersistenceManager(application)
    
    private val _store = MutableStateFlow<BudgetStore?>(null)
    val store: StateFlow<BudgetStore?> = _store.asStateFlow()

    init {
        loadStore()
    }

    private fun loadStore() {
        viewModelScope.launch {
            _store.value = persistenceManager.loadBudgetStore()
        }
    }

    fun updateMonthlySalary(salary: Double) {
        viewModelScope.launch {
            val currentStore = _store.value ?: return@launch
            currentStore.monthlySalary = salary
            _store.value = currentStore
            persistenceManager.saveBudgetStore(currentStore)
        }
    }

    fun updateSavingTarget(target: Double) {
        viewModelScope.launch {
            val currentStore = _store.value ?: return@launch
            currentStore.savingTarget = target
            _store.value = currentStore
            persistenceManager.saveBudgetStore(currentStore)
        }
    }

    fun addTransaction(amount: Double, note: String?, type: TransactionType) {
        viewModelScope.launch {
            val currentStore = _store.value ?: return@launch
            currentStore.addTransaction(amount, note, type)
            _store.value = currentStore
            persistenceManager.saveBudgetStore(currentStore)
        }
    }

    fun removeTransaction(transactionId: String) {
        viewModelScope.launch {
            val currentStore = _store.value ?: return@launch
            currentStore.removeTransaction(transactionId)
            _store.value = currentStore
            persistenceManager.saveBudgetStore(currentStore)
        }
    }

    fun removeAllTransactions() {
        viewModelScope.launch {
            val currentStore = _store.value ?: return@launch
            currentStore.removeAllTransactions()
            _store.value = currentStore
            persistenceManager.saveBudgetStore(currentStore)
        }
    }
}
