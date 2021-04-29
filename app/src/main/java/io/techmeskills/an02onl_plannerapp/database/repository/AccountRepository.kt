package io.techmeskills.an02onl_plannerapp.database.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings.Secure
import io.techmeskills.an02onl_plannerapp.database.dao.AccountsDao
import io.techmeskills.an02onl_plannerapp.database.model.Account
import io.techmeskills.an02onl_plannerapp.datastore.Settings
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class AccountRepository(private val accountsDao: AccountsDao, private val dataStore: Settings, context: Context) {

    @SuppressLint("HardwareIds")
    val phoneId: String = Secure.getString(context.contentResolver, Secure.ANDROID_ID)

    suspend fun createAccount(name: String) {
        withContext(Dispatchers.IO) {
            if (isAccountExists(name).not()) {
                accountsDao.addAccount(Account(name = name))
                dataStore.saveAccountId(accountsDao.getAccountId(name))
                dataStore.saveAccountPos(dataStore.getAccountPos())
            }
        }
    }

    private fun isAccountExists(name: String): Boolean {
        return accountsDao.getAccountCount(name) > 0
    }

    fun checkAnyAccountExist(): Flow<Boolean> =
        accountsDao.getAllAccountsFlow().map {
            it.isNotEmpty()
        }.flowOn(Dispatchers.IO)

    suspend fun switchBetweenAccountsByName(name: String, position: Int) {
        withContext(Dispatchers.IO) {
            dataStore.saveAccountId(accountsDao.getAccountId(name))
            dataStore.saveAccountPos(position)
        }
    }

    fun spinnerData() = accountsDao.getAllAccountsNameFlow().combine(
        getCurrentAccountPosition()
    ) { list, accountPos ->
        list to accountPos
    }

    @ExperimentalCoroutinesApi
    fun getCurrentAccountFlow(): Flow<Account?> {
        return dataStore.getAccountIdFlow().flatMapLatest {
            accountsDao.getAccountFlow(it)
        }
    }

    fun updateCurrentAccountName(newName: String, oldName: String) {
        accountsDao.updateAccountName(newName, oldName)
    }

    fun getCurrentAccountPosition(): Flow<Int> {
        return dataStore.getAccountPosFlow()
    }

    @ExperimentalCoroutinesApi
    suspend fun deleteAccount() {
        accountsDao.deleteAccount(getCurrentAccountFlow().first()!!)
        if (checkAnyAccountExist().first()) {
            dataStore.saveAccountPos(FIRST_POSITION)
            dataStore.saveAccountId(accountsDao.getAllAccountsFlow().first()[FIRST_POSITION].id)
        }
    }

    companion object {
        const val FIRST_POSITION = 0
    }
}