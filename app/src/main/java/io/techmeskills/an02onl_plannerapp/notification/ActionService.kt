package io.techmeskills.an02onl_plannerapp.notification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class ActionService : Service(), KoinComponent {
    private val notificationReceiver: NotificationReceiver by inject()

    override fun onStart(intent: Intent?, startId: Int) {
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}