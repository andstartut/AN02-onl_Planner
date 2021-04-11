package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.MutableLiveData
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel() : CoroutineViewModel() {

    val data: MutableLiveData<List<Note>> = MutableLiveData()

    private fun loadNotes(): List<Note> {
        return notes
    }

    fun addNote(title: String, date: String) {
        launch() {
            notes.add(0, Note(title, date))
        }
    }

    fun editNote(title: String, date: String, position: Int) {
        launch() {
            notes[position].title = title
            notes[position].date = date
        }
    }

    private var notes = mutableListOf(
        Note("Помыть посуду"),
        Note("Забрать пальто из химчистки", "23.03.2021"),
        Note("Позвонить Ибрагиму"),
        Note("Заказать перламутровые пуговицы"),
        Note("Избить соседа за шум ночью"),
        Note("Выпить на неделе с Володей", "22.03.2021"),
        Note("Починить кран"),
        Note("Выбить ковры перед весной"),
        Note("Заклеить сапог жене"),
        Note("Купить картошки"),
        Note("Скачать кино в самолёт", "25.03.2021")
    )

    init {
        data.value = loadNotes()
    }
}

class Note(
    var title: String,
    var date: String? = null
)

