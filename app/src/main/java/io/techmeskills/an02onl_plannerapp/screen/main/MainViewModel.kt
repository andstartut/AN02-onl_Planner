package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import io.techmeskills.an02onl_plannerapp.database.model.Note
import io.techmeskills.an02onl_plannerapp.repository.*
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val noteRepository: NoteRepository,
    private val accountRepository: AccountRepository,
    private val cloudRepository: CloudRepository
) :
    CoroutineViewModel() {

    private val searchFlow = MutableStateFlow("")
    private val sortingFieldFlow = MutableStateFlow(SortingField.NONE)
    private val sortingOrderingFlow = MutableStateFlow(SortingOrder.DESC)

    val currentAccountNotesListLD: LiveData<List<Note>> =
        combine(
            searchFlow,
            sortingFieldFlow,
            sortingOrderingFlow
        ) {
            query, field, ordering ->
Triple(query, field, ordering)
        }.flatMapLatest {
            noteRepository.currentAccountNotesSortedFlow(it.first, it.second, it.third)
        }.asLiveData()

    fun switchOrderingByDate() {
        launch {
            sortingFieldFlow.emit(
                SortingField.DATE
            )
            sortingOrderingFlow.emit(
                if (sortingOrderingFlow.value == SortingOrder.ASC) SortingOrder.DESC else SortingOrder.ASC
            )
        }
    }

    fun switchOrderingByTitle() {
        launch {
            sortingFieldFlow.emit(
                SortingField.TEXT
            )
            sortingOrderingFlow.emit(
                if (sortingOrderingFlow.value == SortingOrder.ASC) SortingOrder.DESC else SortingOrder.ASC
            )
        }
    }

    val spinnerDataLD = accountRepository.spinnerData().asLiveData()

    var progressIndicatorLD = MutableLiveData<Boolean>()

    fun changeAccount(name: String, position: Int) {
        launch {
            accountRepository.switchBetweenAccountsByName(name, position)
        }
    }

    fun addNote(note: Note) {
        launch {
            noteRepository.saveNote(note)
        }
    }

    fun deleteWithUndo(note: Note, callback: (Note) -> Unit) {
        val noteCopy = note.copy()
        launch {
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

    fun sortByDate() {
        launch {
            currentAccountNotesListLD.map { list ->
                println("AAA")
                list.sortedBy {note ->
                    note.date
                }
            }
        }
    }
}