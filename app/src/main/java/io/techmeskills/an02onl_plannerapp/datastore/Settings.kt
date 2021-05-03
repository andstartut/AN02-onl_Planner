package io.techmeskills.an02onl_plannerapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class Settings(val context: Context) {

    fun getAccountNameFlow(): Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[stringPreferencesKey(ACCOUNT_NAME)] ?: ""
        }

    fun getAccountPosFlow(): Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[intPreferencesKey(ACCOUNT_POSITION)] ?: 0
        }

    suspend fun saveAccountName(accountName: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(ACCOUNT_NAME)] = accountName
        }
    }

    suspend fun saveAccountPos(accountPos: Int) {
        context.dataStore.edit { preferences ->
            preferences[intPreferencesKey(ACCOUNT_POSITION)] = accountPos
        }
    }

    suspend fun getAccountName(): String = getAccountNameFlow().first()

    suspend fun getAccountPos(): Int = getAccountPosFlow().first()

    companion object {
        const val ACCOUNT_NAME = "account_name"
        const val ACCOUNT_POSITION = "account_position"
    }
}
