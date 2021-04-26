package io.techmeskills.an02onl_plannerapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class Settings(val context: Context) {

    var accountId = -1L

    fun accountIdFlow(): Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[longPreferencesKey(ACCOUNT_ID)] ?: -1L
        }

    suspend fun saveAccountId(accountId: Long) {
        context.dataStore.edit { preferences ->
            preferences[longPreferencesKey(ACCOUNT_ID)] = accountId
        }
    }

    suspend fun getAccountId(): Long = accountIdFlow().first()

    companion object {
        const val ACCOUNT_ID = "account_id"
    }
}
