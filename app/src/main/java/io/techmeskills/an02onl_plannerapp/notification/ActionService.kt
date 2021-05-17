package io.techmeskills.an02onl_plannerapp.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            noteId = it.getLongExtra(NotificationRepository.INTENT_NOTE_ID, -1)
            when (it.action) {
                NotificationReceiver.ACTION_POSTPONE -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        noteRepository.postponeNoteById(noteId)
                        Toast.makeText(applicationContext, "Do something", Toast.LENGTH_LONG).show()
                    }
                }
                NotificationReceiver.ACTION_DELETE -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        noteRepository.deleteNoteById(noteId)
                        Toast.makeText(applicationContext, "Note deleted", Toast.LENGTH_LONG).show()
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