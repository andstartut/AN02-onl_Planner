package io.techmeskills.an02onl_plannerapp.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import io.techmeskills.an02onl_plannerapp.repository.NoteRepository
import io.techmeskills.an02onl_plannerapp.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class ActionService : Service(), KoinComponent {
    private val noteRepository: NoteRepository by inject()

    private var noteId = -1L

    @SuppressLint("ServiceCast")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationBuilderManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        intent?.let {
            noteId = it.getLongExtra(NotificationRepository.INTENT_NOTE_ID, -1)
            when (it.action) {
                NotificationReceiver.ACTION_POSTPONE -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        noteRepository.postponeNoteById(noteId)
                        notificationBuilderManager.cancel(noteId.toInt())
                    }
                }
                NotificationReceiver.ACTION_DELETE -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        noteRepository.deleteNoteById(noteId)
                        notificationBuilderManager.cancel(noteId.toInt())
                    }
                }
                else -> Unit
            }
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}