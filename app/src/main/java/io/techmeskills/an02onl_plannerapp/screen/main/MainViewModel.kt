package io.techmeskills.an02onl_plannerapp.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.security.auth.callback.Callback

class MainViewModel() : CoroutineViewModel() {

    var data: MutableLiveData<List<Note>> = MutableLiveData()


    fun getNotes(): MutableLiveData<List<Note>> {
        return data
    }

    private fun loadNotes(): List<Note> {
        return notes
    }

    fun addNote(title: String) {
        notes.add(Note(title))
    }
//    fun addNote(title: String) {
//        notes.add(Note(title))
//    }

    private val notes = mutableListOf(
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
    val date: String? = null
)

