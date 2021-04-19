package io.techmeskills.an02onl_plannerapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.techmeskills.an02onl_plannerapp.BuildConfig
import io.techmeskills.an02onl_plannerapp.data.Note
import io.techmeskills.an02onl_plannerapp.data.dao.NotesDao


@Database(entities = [Note::class], version = 2)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}
    object BuildDatabase {
        fun create(context: Context): NotesDatabase =
            Room.databaseBuilder(
                context,
                NotesDatabase::class.java,
                "${BuildConfig.APPLICATION_ID}.db",
            ).addMigrations(MIGRATION_1_2)
                .build()
    }

//    companion object {
//        @Volatile
//        private var instance: NotesDatabase? = null
//
//        fun getInstance(context: Context): NotesDatabase {
//            return instance ?: synchronized(this) {
//                instance ?: buildDatabase(context).also { instance = it }
//            }
//        }
//
//        fun buildDatabase(context: Context): NotesDatabase {
//            return Room.databaseBuilder(
//                context,
//                NotesDatabase::class.java,
//                "io.techmeskills.an02onl_plannerapp.db"
//            ).addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    Log.e(TAG, "onCreate: AAA")
//                    Executors.newSingleThreadExecutor().execute {
//                        Log.e(TAG, "onCreate: AAA")
//                        instance?.notesDao()?.insertNotes(
//                            listOf(
//                                Note(0, "User1", "Помыть посуду", null),
//                                Note(1, "User1", "Забрать пальто из химчистки", "23.03.2021"),
//                                Note(2, "User1", "Позвонить Ибрагиму", null),
//                                Note(3, "User2","Заказать перламутровые пуговицы", null),
//                            )
//                        )
//                    }
//                }
//            }).build()
//        }
//    }
//}


