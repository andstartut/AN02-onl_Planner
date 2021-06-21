package io.techmeskills.an02onl_plannerapp.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.database.model.Note
import io.techmeskills.an02onl_plannerapp.datastore.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NoteRepository(
    private val notesDao: NotesDao,
    private val dataStore: Settings,
    private val notificationRepository: NotificationRepository
) {

    val currentAccountNotesFlow: Flow<List<Note>> =
        dataStore.getAccountNameFlow().flatMapLatest { accountName ->
            notesDao.getAccountNotesFlow(accountName)
        }.flowOn(Dispatchers.IO)

    fun currentAccountNotesSortedFlow(
        query: String,
        sortingField: SortingField,
        sortingOrder: SortingOrder
    ): Flow<List<Note>> =
        dataStore.getAccountNameFlow()
            .flatMapLatest { accountName ->
                notesDao.getAccountNotesSortedByDateRawFlow(
                    getSortedQuery(
                        accountName,
                        query,
                        sortingField,
                        sortingOrder
                    )
                )
            }.flowOn(Dispatchers.IO)

    private fun getSortedQuery(
        accountName: String,
        query: String,
        sortingField: SortingField,
        sortingOrder: SortingOrder
    ): SimpleSQLiteQuery {
        return SimpleSQLiteQuery(
            "SELECT * FROM notes WHERE accountName == '$accountName' AND title LIKE '%$query%' ORDER BY pinned AND ${sortingField.value} ${sortingOrder.value}"
        )
    }

    suspend fun saveNote(note: Note) {
        withContext(Dispatchers.IO) {
//            notesDao.insertNote(
//                Note(
//                    id = note.id,
//                    title = note.title,
//                    date = note.date,
//                    setEvent = note.setEvent,
//                    accountName = dataStore.getAccountName()
//                )
//            )
            val id = notesDao.insertNote(
                note.copy(accountName = dataStore.getAccountName())
            )
            if (note.setEvent) {
                notificationRepository.setNotification(note.copy(id = id))
            }
        }
    }

    suspend fun saveNotes(notes: List<Note>) {
        withContext(Dispatchers.IO) {
            notesDao.insertNotes(notes)
        }
    }

    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.updateNote(note)
            if (note.setEvent) {
                notificationRepository.setNotification(note)
            }
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.deleteNote(note)
        }
    }

    suspend fun deleteNoteById(noteId: Long) {
        withContext(Dispatchers.IO) {
            notesDao.getNote(noteId).let { note ->
                notesDao.deleteNote(note)
            }
        }
    }

    suspend fun setAllNotesSyncWithCloud() {
        withContext(Dispatchers.IO) {
            notesDao.setAllNotesSyncWithCloud()
        }
    }

    suspend fun postponeNoteById(noteId: Long) {
        withContext(Dispatchers.IO) {
            notesDao.getNote(noteId).let { note ->
                val postponedNote = notificationRepository.postponeNoteTimeByFiveMinutes(note)
                notesDao.updateNote(postponedNote)
                notificationRepository.setNotification(postponedNote)
            }
        }
    }

    suspend fun pinNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.pinNote(note.id, note.pinned.not())
        }
    }
}

enum class SortingField(val value: String) {
    TEXT("title"),
    DATE("date"),
    NONE("id")
}

enum class SortingOrder(val value: String) {
    ASC("ASC"),
    DESC("DESC")
}