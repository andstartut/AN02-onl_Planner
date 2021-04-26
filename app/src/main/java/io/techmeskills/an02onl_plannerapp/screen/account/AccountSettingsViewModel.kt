package io.techmeskills.an02onl_plannerapp.screen.account

import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.database.repository.AccountRepository
import io.techmeskills.an02onl_plannerapp.database.repository.NoteRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class AccountSettingsViewModel(
    private val noteRepository: NoteRepository,
    private val accountRepository: AccountRepository
) : CoroutineViewModel() {

    val currentAccountNameLiveData = accountRepository.getCurrentAccountNameFlow().asLiveData()

    val currentAccountFlow = accountRepository.getCurrentAccountFlow()

    fun changeAccountName(newName: String) {
        launch {
            accountRepository.updateCurrentAccountName(newName)
            noteRepository.updateAccountIdForAllNotes(currentAccountFlow)
        }
    }
}