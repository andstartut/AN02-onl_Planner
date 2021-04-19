package io.techmeskills.an02onl_plannerapp

import android.app.Application
import io.techmeskills.an02onl_plannerapp.data.PersistentStorage
import io.techmeskills.an02onl_plannerapp.data.db.BuildDatabase
import io.techmeskills.an02onl_plannerapp.data.db.NotesDatabase
import io.techmeskills.an02onl_plannerapp.screen.login.LoginViewModel
import io.techmeskills.an02onl_plannerapp.screen.main.MainViewModel
import io.techmeskills.an02onl_plannerapp.screen.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PlannerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlannerApp)
            modules(listOf(
                viewModels,
                databaseModule,
                storageModule
            ))
        }
    }

    private val viewModels = module {
        viewModel { SplashViewModel() }
        viewModel { MainViewModel(get(), get()) }
        viewModel { LoginViewModel(get()) }
    }

    private val databaseModule = module {
        single { BuildDatabase.create(get()) }
        factory { get<NotesDatabase>().notesDao() }
    }

    private val storageModule = module {
        single { PersistentStorage(this.androidContext()) }
    }
}