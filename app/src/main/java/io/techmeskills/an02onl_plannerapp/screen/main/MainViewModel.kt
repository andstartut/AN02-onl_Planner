package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import io.techmeskills.an02onl_plannerapp.database.model.Note
import io.techmeskills.an02onl_plannerapp.repository.AccountRepository
import io.techmeskills.an02onl_plannerapp.repository.CloudRepository
import io.techmeskills.an02onl_plannerapp.repository.NoteRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class MainViewModel(
    private val noteRepository: NoteRepository,
    private val accountRepository: AccountRepository,
    private val cloudRepository: CloudRepository
) :
    CoroutineViewModel() {

    val currentAccountNotesListLD = noteRepository.currentAccountNotesFlow.asLiveData()

    val spinnerDataLD = accountRepository.spinnerData().asLiveData()

    var progressIndicatorLD = MutableLiveData<Boolean>()

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

    fun deleteWithUndo(note: Note, callback: (Note) -> Unit) {
        val noteCopy = Note(
            id = note.id,
            title = note.title,
            date = note.date,
            accountName = note.accountName,
            setEvent = note.setEvent
        )
        launch() {
            noteRepository.deleteNote(note)
        }
        callback(noteCopy)
    }

    @ExperimentalCoroutinesApi
    fun importNotes() {
        launch {
            val importNotes = cloudRepository.importNotes()
            progressIndicatorLD.postValue(importNotes)
        }
    }

    @ExperimentalCoroutinesApi
    fun exportNotes() {
        launch {
            val exportNotes = cloudRepository.exportNotes()
            progressIndicatorLD.postValue(exportNotes)
        }
    }
}