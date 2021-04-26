package io.techmeskills.an02onl_plannerapp.screen.account

import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.database.repository.AccountRepository
import io.techmeskills.an02onl_plannerapp.database.repository.NoteRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class AccountSettingsViewModel(
    private val accountRepository: AccountRepository
) : CoroutineViewModel() {

    val currentAccountLD = accountRepository.getCurrentAccountFlow().asLiveData()

    fun changeAccountName(newName: String) {
        launch {
            accountRepository.updateCurrentAccountName(
                newName,
                currentAccountLD.value?.name!!
            )
        }
    }
}