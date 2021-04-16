package io.techmeskills.an02onl_plannerapp.data.db

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.techmeskills.an02onl_plannerapp.data.Note
import io.techmeskills.an02onl_plannerapp.data.dao.NotesDao
import java.util.concurrent.Executors


@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        @Volatile
        private var instance: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        fun buildDatabase(context: Context): NotesDatabase {
            return Room.databaseBuilder(
                context,
                NotesDatabase::class.java,
                "io.techmeskills.an02onl_plannerapp.db"
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.e(TAG, "onCreate: AAA")
                    Executors.newSingleThreadExecutor().execute {
                        Log.e(TAG, "onCreate: AAA")
                        instance?.notesDao()?.insertNotes(
                            listOf(
                                Note(0, "Помыть посуду", null),
                                Note(1, "Забрать пальто из химчистки", "23.03.2021"),
                                Note(2, "Позвонить Ибрагиму", null),
                            )
                        )
                    }
                }
            }).build()
        }
    }
}

/*
object BuildDatabase {
    fun create(context: Context): NotesDatabase =
        Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            "io.techmeskills.an02onl_plannerapp.db",
        ).build()
*/
