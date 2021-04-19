package io.techmeskills.an02onl_plannerapp.data

import android.content.Context
import android.content.SharedPreferences

class PersistentStorage(contextValue: Context?) {
    private var settings: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var context: Context? = contextValue

    private fun init() {
        settings = context!!.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
        editor = settings!!.edit()
    }

    fun addProperty(property: String, value: String) {
        if (settings == null) {
            init()
        }
        editor!!.putString(property, value)
        editor!!.apply()
    }

    fun setAccountName(value: String) {
        if (settings == null) {
            init()
        }
        editor!!.putString(ACCOUNT_NAME, value)
        editor!!.apply()
    }

    fun getAccountName(): String? {
        if (settings == null) {
            init()
        }
        return settings!!.getString(ACCOUNT_NAME, null);
    }

    fun getProperty(property: String): String? {
        if (settings == null) {
            init()
        }
        return settings!!.getString(property, null);
    }

    companion object {
        private const val STORAGE_NAME = "Accounts"
        private const val ACCOUNT_NAME = "AccountName"
    }
}