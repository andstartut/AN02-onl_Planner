package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import io.techmeskills.an02onl_plannerapp.support.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel() : CoroutineViewModel() {

    val data: MutableLiveData<List<Note>> = MutableLiveData()

    private var notes = mutableListOf(
        Note(0, "Помыть посуду"),
        Note(1, "Забрать пальто из химчистки", "23.03.2021"),
        Note(2, "Позвонить Ибрагиму"),
        Note(3, "Заказать перламутровые пуговицы"),
        Note(4, "Избить соседа за шум ночью"),
        Note(5, "Выпить на неделе с Володей", "22.03.2021"),
        Note(6, "Починить кран"),
        Note(7, "Выбить ковры перед весной"),
        Note(8, "Заклеить сапог жене"),
        Note(9, "Купить картошки"),
        Note(10, "Скачать кино в самолёт", "25.03.2021")
    )

    fun addNote(note: Note) {
        launch() {
            notes.add(0, Note((notes.maxByOrNull { it.id }?.id ?: 0) + 1, note.title, note.date))
            data.postValue(notes)
        }
    }

    fun editNote(note: Note) {
        launch() {
            val position = notes.indexOfFirst { note.id == it.id }
            notes.removeAt(position)
            notes.add(position, note)
            data.postValue(notes)
        }
    }

    fun deleteNote(position: Int) {
        launch(Dispatchers.Main) {
            notes.removeAt(position)
            data.postValue(notes)
        }
    }

    private fun loadNotes(): List<Note> {
        return notes
    }

    init {
        data.value = loadNotes()
    }
}

class Note(
    var id: Int,
    var title: String,
    var date: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0!!.writeInt(id)
        p0.writeString(title)
        p0.writeString(date)
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }

}

