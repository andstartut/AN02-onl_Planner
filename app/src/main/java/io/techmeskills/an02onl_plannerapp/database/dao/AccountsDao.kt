package io.techmeskills.an02onl_plannerapp.database.dao

import androidx.room.*
import io.techmeskills.an02onl_plannerapp.database.model.Account
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AccountsDao {
    @Insert
    abstract fun addAccount(account: Account): Long

    @Delete
    abstract fun deleteAccount(account: Account)

    @Update
    abstract fun updateAccount(account: Account)

//    @Query("SELECT * FROM accounts WHERE name == :name")
//    abstract fun getAccount(name: String): Account
//
//    @Query("SELECT * FROM accounts WHERE name == :name")
//    abstract fun getAccountFlow(name: String): Flow<Account>

    @Query("SELECT * FROM accounts")
    abstract fun getAllAccounts(): List<Account>

    @Query("SELECT * FROM accounts")
    abstract fun getAllAccountsFlow(): Flow<List<Account>>

    @Query("SELECT name FROM accounts")
    abstract fun getAllAccountsNamesFlow(): Flow<List<String>>

    @Query("SELECT COUNT(name) FROM accounts")
    abstract fun getAllAccountsCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM accounts WHERE name == :accountName")
    abstract fun getAccountCount(accountName: String): Int

    @Query("UPDATE accounts SET name = :newName WHERE name = :oldName")
    abstract fun updateAccountName(newName: String, oldName: String)
}