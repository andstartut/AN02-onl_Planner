package io.techmeskills.an02onl_plannerapp.database.repository

import io.techmeskills.an02onl_plannerapp.database.dao.AccountsDao
import io.techmeskills.an02onl_plannerapp.database.model.Account
import io.techmeskills.an02onl_plannerapp.datastore.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AccountRepository(private val accountsDao: AccountsDao, private val dataStore: Settings) {

    suspend fun createAccount(name: String) {
        withContext(Dispatchers.IO) {
            if (isAccountExists(name).not()) {
                accountsDao.addAccount(Account(name = name))
                dataStore.saveAccountId(accountsDao.getAccountId(name))
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

    fun getAllAccountsFlow(): Flow<List<String>> {
        return accountsDao.getAllAccountsFlow()
    }

    fun getCurrentAccountNameFlow(): Flow<String> {
        return dataStore.accountIdFlow().map {
            accountsDao.getAccountName(it)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun switchBetweenAccountsByName(name: String) {
        withContext(Dispatchers.IO) {
            dataStore.saveAccountId(accountsDao.getAccountId(name))
        }
    }

    fun getCurrentAccountFlow(): Flow<Account> {
        return dataStore.accountIdFlow().map {
            accountsDao.getAccount(it)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateCurrentAccountName(newName: String) {
        withContext(Dispatchers.IO) {
            dataStore.saveAccountId(accountsDao.getAccountId(newName))
            dataStore.accountIdFlow().map {
                val currentAccount = accountsDao.getAccount(it)
                accountsDao.updateAccount(
                    Account(
                        id = currentAccount.id,
                        name = newName
                    )
                )
            }
        }
    }
}