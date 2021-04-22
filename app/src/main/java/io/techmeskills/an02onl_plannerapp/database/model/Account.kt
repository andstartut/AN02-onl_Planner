package io.techmeskills.an02onl_plannerapp.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "accounts", indices = [Index(value = ["name"], unique = true)])
open class Account(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String
): Parcelable