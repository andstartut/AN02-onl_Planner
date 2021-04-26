package io.techmeskills.an02onl_plannerapp.database.repository

import io.techmeskills.an02onl_plannerapp.database.dao.NotesDao
import io.techmeskills.an02onl_plannerapp.database.model.Note
import io.techmeskills.an02onl_plannerapp.datastore.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NoteRepository(private val notesDao: NotesDao, private val dataStore: Settings) {

    val currentAccountNotesFlow: Flow<List<Note>> =
        dataStore.getAccountIdFlow()
            .flatMapLatest { accountId ->
                notesDao.getAllAccountNotesFlow(accountId)
            }.flowOn(Dispatchers.IO)

    suspend fun saveNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.insertNote(
                Note(
                    id = note.id,
                    title = note.title,
                    date = note.date,
                    accountId = dataStore.getAccountId()
                )
            )
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
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.deleteNote(note)
        }
    }

    suspend fun setAllNotesSyncWithCloud() {
        withContext(Dispatchers.IO) {
            notesDao.setAllNotesSyncWithCloud()
        }
    }
}