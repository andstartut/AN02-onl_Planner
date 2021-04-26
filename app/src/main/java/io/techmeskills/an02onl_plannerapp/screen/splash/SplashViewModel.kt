package io.techmeskills.an02onl_plannerapp.screen.splash

import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.database.repository.AccountRepository
import io.techmeskills.an02onl_plannerapp.datastore.Settings
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class SplashViewModel(
    private val accountRepository: AccountRepository
) : CoroutineViewModel() {

    val checkAnyAccountExistLiveData = accountRepository.checkAnyAccountExist().asLiveData()
//    fun isAccountExist(): Boolean {
//        var accountId = -1L
//        launch {
//            accountId = dataStore.getAccountId()
//        }
//        return accountId >= 0L
//    }
}