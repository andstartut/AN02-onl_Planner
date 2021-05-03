package io.techmeskills.an02onl_plannerapp.screen.noteDetails

import io.techmeskills.an02onl_plannerapp.database.model.Note
import io.techmeskills.an02onl_plannerapp.database.repository.AccountRepository
import io.techmeskills.an02onl_plannerapp.database.repository.NoteRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NoteDetailsViewModel(
    private val noteRepository: NoteRepository
) : CoroutineViewModel() {

    @ExperimentalCoroutinesApi
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
}