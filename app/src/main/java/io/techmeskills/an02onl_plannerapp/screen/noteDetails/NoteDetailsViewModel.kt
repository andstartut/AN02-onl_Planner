package io.techmeskills.an02onl_plannerapp.screen.noteDetails

import androidx.lifecycle.MutableLiveData
import io.techmeskills.an02onl_plannerapp.database.model.Note
import io.techmeskills.an02onl_plannerapp.repository.NoteRepository
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*

class NoteDetailsViewModel(
    private val noteRepository: NoteRepository
) : CoroutineViewModel() {

    var date = MutableLiveData<Date>()

    @ExperimentalCoroutinesApi
    fun addNote(note: Note) {
        launch {
            noteRepository.saveNote(note)
        }
    }

    fun editNote(note: Note) {
        launch {
            noteRepository.updateNote(note)
        }
    }

    fun setDate(setDate: Date) {
        launch {
            date.postValue(setDate)
        }
    }
}