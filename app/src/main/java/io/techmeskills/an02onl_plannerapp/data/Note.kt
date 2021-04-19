package io.techmeskills.an02onl_plannerapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes")
open class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val date: String?,
    val accountName: String?
) : Parcelable