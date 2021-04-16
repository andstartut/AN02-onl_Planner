package io.techmeskills.an02onl_plannerapp.screen.main

import io.techmeskills.an02onl_plannerapp.data.Note
import io.techmeskills.an02onl_plannerapp.data.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch

class MainViewModel(private val notesDao: NotesDao) : CoroutineViewModel() {

    val data = notesDao.getAllNotesLifeData()

    val noteList = listOf(
        Note(0, "Помыть посуду", null),
        Note(1, "Забрать пальто из химчистки", "23.03.2021"),
        Note(2, "Позвонить Ибрагиму", null),
        Note(3, "Заказать перламутровые пуговицы", null),
        Note(4, "Избить соседа за шум ночью", null),
        Note(5, "Выпить на неделе с Володей", "22.03.2021"),
        Note(6, "Починить кран", null),
        Note(7, "Выбить ковры перед весной", null),
        Note(8, "Заклеить сапог жене", null),
        Note(9, "Купить картошки", null),
        Note(10, "Скачать кино в самолёт", "25.03.2021")
    )

    fun addNote(note: Note) {
        launch() {
            notesDao.insertNote(note)
        }
    }

    fun editNote(note: Note) {
        launch() {
            notesDao.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        launch() {
            notesDao.deleteNote(note)
        }
    }
}