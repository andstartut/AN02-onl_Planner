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

    @Query("SELECT * FROM accounts WHERE id == :id")
    abstract fun getAccount(id: Long): Account

    @Query("SELECT name FROM accounts")
    abstract fun getAllAccountsFlow(): Flow<List<String>>

    @Query("SELECT COUNT(id) FROM accounts")
    abstract fun getAllAccountsCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM accounts WHERE name == :accountName")
    abstract fun getAccountCount(accountName: String): Int

    @Query("SELECT name FROM accounts WHERE id == :id")
    abstract fun getAccountName(id: Long): String

    @Query("SELECT id FROM accounts WHERE name == :accountName")
    abstract fun getAccountId(accountName: String): Long
}