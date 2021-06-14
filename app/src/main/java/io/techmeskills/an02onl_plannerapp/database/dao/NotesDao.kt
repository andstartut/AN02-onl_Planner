package io.techmeskills.an02onl_plannerapp.database.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import io.techmeskills.an02onl_plannerapp.database.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertNote(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertNotes(notes: List<Note>): List<Long>

    @Delete
    abstract fun deleteNote(note: Note)

    @Update
    abstract fun updateNote(note: Note)

    @Query("SELECT * FROM notes")
    abstract fun getAllNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE id == :noteId LIMIT 1")
    abstract fun getNote(noteId: Long): Note

    @Query("SELECT * FROM notes ORDER BY id ASC")
    abstract fun getAllNotesFlow(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE accountName == :accountName ORDER BY id DESC")
    abstract fun getAccountNotesFlow(accountName: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE accountName == :accountName ORDER BY ABS(:currentTime - date)")
    abstract fun getAccountNotesSortedByDate(accountName: String, currentTime: Long): Flow<List<Note>>

    @RawQuery(observedEntities = [Note::class])
    abstract fun getAccountNotesSortedByDateRawFlow(query: SimpleSQLiteQuery): Flow<List<Note>>

    @Query("UPDATE notes SET cloudSync = 1")
    abstract fun setAllNotesSyncWithCloud()
}