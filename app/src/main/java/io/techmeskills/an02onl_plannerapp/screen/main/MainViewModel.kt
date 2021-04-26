package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.database.model.Note
import io.techmeskills.an02onl_plannerapp.database.repository.AccountRepository
import io.techmeskills.an02onl_plannerapp.database.repository.CloudRepository
import io.techmeskills.an02onl_plannerapp.database.repository.NoteRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val noteRepository: NoteRepository,
    private val accountRepository: AccountRepository,
    private val cloudRepository: CloudRepository
) :
    CoroutineViewModel() {

    val currentAccountNotesListLD = noteRepository.currentAccountNotesFlow.asLiveData()

    val spinnerDataLD = accountRepository.spinnerData().asLiveData()

    fun changeAccount(name: String, position: Int) {
        launch {
            accountRepository.switchBetweenAccountsByName(name, position)
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

    fun importNotes() {
        launch {
            cloudRepository.importNotes()
        }
    }

    fun exportNotes() {
        launch {
            cloudRepository.exportNotes()
        }
    }
}