package com.smartsaving.android.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "budget_store")

class PersistenceManager(private val context: Context) {
    companion object {
        private val MONTHLY_SALARY_KEY = doublePreferencesKey("monthly_salary")
        private val SAVING_TARGET_KEY = doublePreferencesKey("saving_target")
        private val TRANSACTIONS_KEY = stringPreferencesKey("transactions")
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun saveBudgetStore(store: BudgetStore) {
        context.dataStore.edit { preferences ->
            preferences[MONTHLY_SALARY_KEY] = store.monthlySalary
            preferences[SAVING_TARGET_KEY] = store.savingTarget
            
            val transactionsJson = json.encodeToString(store.transactions)
            preferences[TRANSACTIONS_KEY] = transactionsJson
        }
    }

    suspend fun loadBudgetStore(): BudgetStore {
        val preferences = context.dataStore.data.first()
        val monthlySalary = preferences[MONTHLY_SALARY_KEY] ?: 0.0
        val savingTarget = preferences[SAVING_TARGET_KEY] ?: 0.0
        
        val transactionsJson = preferences[TRANSACTIONS_KEY] ?: "[]"
        val transactions = try {
            json.decodeFromString<List<Transaction>>(transactionsJson)
        } catch (e: Exception) {
            emptyList()
        }
        
        val store = BudgetStore(monthlySalary, savingTarget, transactions.toMutableList())
        store.transactions = transactions // 触发flow更新
        return store
    }

    fun observeBudgetStore(): Flow<BudgetStore> {
        return context.dataStore.data.map { preferences ->
            val monthlySalary = preferences[MONTHLY_SALARY_KEY] ?: 0.0
            val savingTarget = preferences[SAVING_TARGET_KEY] ?: 0.0
            
            val transactionsJson = preferences[TRANSACTIONS_KEY] ?: "[]"
            val transactions = try {
                json.decodeFromString<List<Transaction>>(transactionsJson)
            } catch (e: Exception) {
                emptyList()
            }
            
            BudgetStore(monthlySalary, savingTarget, transactions.toMutableList()).apply {
                this.transactions = transactions
            }
        }
    }
}
