package io.techmeskills.an02onl_plannerapp.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import io.techmeskills.an02onl_plannerapp.database.model.Note
import io.techmeskills.an02onl_plannerapp.notification.NotificationReceiver

class NotificationRepository(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun createIntent(note: Note): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.action = INTENT_NOTE_ACTION
        intent.putExtra(INTENT_NOTE_ACCOUNT, note.accountName)
        intent.putExtra(INTENT_NOTE_TITLE, note.title)
        intent.putExtra(INTENT_NOTE_ID, note.id)
        return PendingIntent.getBroadcast(
            context,
            0,
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

    companion object {
        const val INTENT_NOTE_TITLE = "title"
        const val INTENT_NOTE_ACCOUNT = "account"
        const val INTENT_NOTE_ACTION = "planner"
        const val INTENT_NOTE_ID = "id"

    }
}