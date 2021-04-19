package io.techmeskills.an02onl_plannerapp.screen.main

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.map
import io.techmeskills.an02onl_plannerapp.data.Note
import io.techmeskills.an02onl_plannerapp.data.PersistentStorage
import io.techmeskills.an02onl_plannerapp.data.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.launch
import org.koin.dsl.koinApplication

class MainViewModel(private val notesDao: NotesDao, persistentStorage: PersistentStorage) : CoroutineViewModel() {

    var data = notesDao.getAccountNotesLifeData(persistentStorage.getAccountName()!!)

    val accountsList = listOf(
        "User1",
        "User2"
    )

//    val noteList = listOf(
//        Note(0, "Помыть посуду", null),
//        Note(1, "Забрать пальто из химчистки", "23.03.2021"),
//        Note(2, "Позвонить Ибрагиму", null),
//        Note(3, "Заказать перламутровые пуговицы", null),
//        Note(4, "Избить соседа за шум ночью", null),
//        Note(5, "Выпить на неделе с Володей", "22.03.2021"),
//        Note(6, "Починить кран", null),
//        Note(7, "Выбить ковры перед весной", null),
//        Note(8, "Заклеить сапог жене", null),
//        Note(9, "Купить картошки", null),
//        Note(10, "Скачать кино в самолёт", "25.03.2021")
//    )

    fun updateLiveData(accountName: String) {
        data = notesDao.getAccountNotesLifeData(accountName)
    }

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

    fun deleteWithUndo(note: Note, callback: (Note) -> Unit) {
        val copy = Note(
            id = note.id,
            accountName = note.accountName,
            title = note.title,
            date = note.date
        )
        deleteNote(note)
        callback(copy)
    }
}