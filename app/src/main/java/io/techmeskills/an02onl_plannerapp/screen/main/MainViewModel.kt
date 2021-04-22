package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.database.model.Note
import io.techmeskills.an02onl_plannerapp.database.repository.AccountRepository
import io.techmeskills.an02onl_plannerapp.database.repository.NoteRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(private val noteRepository: NoteRepository, private val accountRepository: AccountRepository) :
    CoroutineViewModel() {

    init {
        accountRepository.getCurrentAccountNameFlow().map {
            //lastAccountName = it
        }
    }
    var lastAccountName: String? = null

    val currentAccountNotesLiveData = noteRepository.currentAccountNotesFlow.asLiveData()

    val accountsLiveData = accountRepository.getAllAccountsFlow().asLiveData()


    fun changeAccount(name: String) {
        launch {
            accountRepository.switchBetweenAccountsByName(name)
        }
    }

    fun addNote(note: Note) {
        launch() {
            noteRepository.saveNote(note)
        }
    }

    fun editNote(note: Note) {
        launch() {
            noteRepository.updateNote(note)
        }
    }

    fun deleteWithUndo(note: Note, callback: (Note) -> Unit) {
        val noteCopy = Note(
            id = note.id,
            title = note.title,
            date = note.date,
            accountId = note.accountId,
        )
        launch() {
            noteRepository.deleteNote(note)
        }
        callback(noteCopy)
    }
}