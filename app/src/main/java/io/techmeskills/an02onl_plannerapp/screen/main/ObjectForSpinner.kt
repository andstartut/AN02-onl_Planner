package io.techmeskills.an02onl_plannerapp.screen.main

import io.techmeskills.an02onl_plannerapp.database.model.Account

class ObjectForSpinner(
    val accountNamesList: List<String> = listOf(),
    val currentAccount: Account = Account(0L, "")
) {
}