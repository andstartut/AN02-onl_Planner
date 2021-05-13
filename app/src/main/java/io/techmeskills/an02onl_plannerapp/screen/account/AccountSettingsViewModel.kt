package io.techmeskills.an02onl_plannerapp.screen.account

import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.repository.AccountRepository
import io.techmeskills.an02onl_plannerapp.repository.CloudRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class AccountSettingsViewModel(
    private val accountRepository: AccountRepository,
    private val cloudRepository: CloudRepository
) : CoroutineViewModel() {

    val currentAccountNameLD = accountRepository.getCurrentAccountNameFlow().asLiveData()

    val checkAnyAccountExist = accountRepository.checkAnyAccountExist().asLiveData()

    fun changeAccountName(newName: String) {
        launch {
            accountRepository.updateCurrentAccountName(
                newName,
                accountRepository.getCurrentAccountName()
            )
        }
    }

    fun deleteAccount() {
        launch {
            accountRepository.deleteAccount()
        }
    }
}