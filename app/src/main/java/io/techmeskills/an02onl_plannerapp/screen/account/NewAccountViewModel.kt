package io.techmeskills.an02onl_plannerapp.screen.account

import io.techmeskills.an02onl_plannerapp.database.repository.AccountRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class NewAccountViewModel(private val accountRepository: AccountRepository) : CoroutineViewModel() {

    fun createNewAccount(name: String) {
        launch {
            accountRepository.createAccount(name)
        }
    }
}