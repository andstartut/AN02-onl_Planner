package io.techmeskills.an02onl_plannerapp.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import io.techmeskills.an02onl_plannerapp.database.model.Note
import io.techmeskills.an02onl_plannerapp.datastore.Settings
import io.techmeskills.an02onl_plannerapp.notification.NotificationReceiver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext


class NotificationRepository(private val context: Context, private val dataStore: Settings) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private var accountName = ""
    init {
        GlobalScope.launch {
            accountName = dataStore.getAccountName()
        }
    }

    private fun createIntent(note: Note): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.action = INTENT_NOTE_ACTION
        intent.putExtra(INTENT_NOTE_ACCOUNT, accountName)
        intent.putExtra(INTENT_NOTE_TITLE, note.title)
        intent.putExtra(INTENT_NOTE_ID, note.id)
        return PendingIntent.getBroadcast(
            context,
            note.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun setNotification(note: Note) {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, note.date, createIntent(note))
    }

    fun undoNotification(note: Note) {
        alarmManager.cancel(createIntent(note))
    }

    fun postponeNoteTimeByFiveMinutes(note: Note): Note {
        val calendar = Calendar.getInstance()
        calendar.time = Date(note.date)
        calendar.add(Calendar.MINUTE, 5)
        return note.copy(date = calendar.time.time)
    }

    companion object {
        const val INTENT_NOTE_TITLE = "title"
        const val INTENT_NOTE_ACCOUNT = "account"
        const val INTENT_NOTE_ACTION = "planner"
        const val INTENT_NOTE_ID = "id"

    }
}