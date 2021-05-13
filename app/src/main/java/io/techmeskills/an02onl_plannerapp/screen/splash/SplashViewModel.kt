package io.techmeskills.an02onl_plannerapp.screen.splash

import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.repository.AccountRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel

class SplashViewModel(
    private val accountRepository: AccountRepository
) : CoroutineViewModel() {

    val checkAnyAccountExistLiveData = accountRepository.checkAnyAccountExist().asLiveData()
}