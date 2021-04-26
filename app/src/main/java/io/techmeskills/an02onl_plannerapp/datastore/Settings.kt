package io.techmeskills.an02onl_plannerapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class Settings(val context: Context) {

    fun getAccountIdFlow(): Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[longPreferencesKey(ACCOUNT_ID)] ?: -1L
        }

    fun getAccountPosFlow(): Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[intPreferencesKey(ACCOUNT_POSITION)] ?: 0
        }

    suspend fun saveAccountId(accountId: Long) {
        context.dataStore.edit { preferences ->
            preferences[longPreferencesKey(ACCOUNT_ID)] = accountId
        }
    }

    suspend fun saveAccountPos(accountPos: Int) {
        context.dataStore.edit { preferences ->
            preferences[intPreferencesKey(ACCOUNT_POSITION)] = accountPos
        }
    }

    suspend fun getAccountId(): Long = getAccountIdFlow().first()

    suspend fun getAccountPos(): Int = getAccountPosFlow().first()

    companion object {
        const val ACCOUNT_ID = "account_id"
        const val ACCOUNT_POSITION = "account_position"
    }
}
