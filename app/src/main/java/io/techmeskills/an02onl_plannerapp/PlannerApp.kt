package io.techmeskills.an02onl_plannerapp

import android.app.Application
import io.techmeskills.an02onl_plannerapp.database.db.BuildDataBase
import io.techmeskills.an02onl_plannerapp.database.db.NotesDatabase
import io.techmeskills.an02onl_plannerapp.database.repository.AccountRepository
import io.techmeskills.an02onl_plannerapp.database.repository.NoteRepository
import io.techmeskills.an02onl_plannerapp.datastore.Settings
import io.techmeskills.an02onl_plannerapp.screen.account.AccountSettingsViewModel
import io.techmeskills.an02onl_plannerapp.screen.account.NewAccountViewModel
import io.techmeskills.an02onl_plannerapp.screen.main.MainViewModel
import io.techmeskills.an02onl_plannerapp.screen.noteDetails.NoteDetailsViewModel
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
                dataBaseModule,
                dataStoreModule,
                repositoryModule
            ))
        }
    }

    private val viewModels = module {
        viewModel { SplashViewModel(get()) }
        viewModel { NewAccountViewModel(get()) }
        viewModel { MainViewModel(get(), get()) }
        viewModel { NoteDetailsViewModel() }
        viewModel { AccountSettingsViewModel(get(), get()) }
    }

    private val dataBaseModule = module {
        single { BuildDataBase.create(get()) }
        factory { get<NotesDatabase>().notesDao() }
        factory { get<NotesDatabase>().accountsDao() }
    }

    private val dataStoreModule = module {
        single { Settings(get()) }
    }

    private val repositoryModule = module {  //создаем репозитории
        factory { AccountRepository(get(), get()) }
        factory { NoteRepository(get(), get()) }
    }
}