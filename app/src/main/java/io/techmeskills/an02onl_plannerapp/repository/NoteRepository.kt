package io.techmeskills.an02onl_plannerapp.repository

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
        dataStore.getAccountNameFlow()
            .flatMapLatest { accountName ->
                notesDao.getAllAccountNotesFlow(accountName)
            }.flowOn(Dispatchers.IO)

    suspend fun saveNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.insertNote(
                Note(
                    title = note.title,
                    date = note.date,
                    setEvent = note.setEvent,
                    accountName = dataStore.getAccountName()
                )
            )
            if (note.setEvent) {
                notificationRepository.setNotification(note)
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
            notificationRepository.undoNotification(note)
            notesDao.updateNote(note)
            if (note.setEvent) {
                notificationRepository.setNotification(note)
            }
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.deleteNote(note)
            notificationRepository.undoNotification(note)
        }
    }

    suspend fun setAllNotesSyncWithCloud() {
        withContext(Dispatchers.IO) {
            notesDao.setAllNotesSyncWithCloud()
        }
    }
}