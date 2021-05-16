package io.techmeskills.an02onl_plannerapp.database.model

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "notes",
    indices = [Index(value = ["title"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["name"],
        childColumns = ["accountName"],
        onUpdate = CASCADE,
        onDelete = CASCADE
    )]
)
open class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val date: Long,
    @ColumnInfo(index = true, name = "accountName")
    val accountName: String,
    val cloudSync: Boolean = false,
    val setEvent: Boolean = false
) : Parcelable