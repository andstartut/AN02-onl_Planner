package io.techmeskills.an02onl_plannerapp.database.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes", indices = [Index(value = ["title"], unique = true)])
open class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "title")
    val title: String,
    val date: String?,
    val accountId: Long,
    val cloudSync: Boolean = false
) : Parcelable