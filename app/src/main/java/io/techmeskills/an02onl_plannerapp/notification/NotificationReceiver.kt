package io.techmeskills.an02onl_plannerapp.notification

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import io.techmeskills.an02onl_plannerapp.BuildConfig
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.repository.NotificationRepository
import org.koin.core.component.KoinApiExtension


class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context, intent)
    }

    @KoinApiExtension
    private fun showNotification(context: Context, intent: Intent) {
        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel(notificationManager, context)
            }
            val title = intent.getStringExtra(NotificationRepository.INTENT_NOTE_ACCOUNT)
            val text = intent.getStringExtra(NotificationRepository.INTENT_NOTE_TITLE)
            val id = intent.getLongExtra(NotificationRepository.INTENT_NOTE_ID, -1)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add)
                .setContentText(text)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .addAction(R.drawable.baseline_delete_20, "I did it", deleteAction(context, id))
                .addAction(deleteAction(context, id))
                .addAction(R.drawable.baseline_delete_24, "Set new time", newTimeAction(context))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setVibrate(longArrayOf(1000, 1000))
                .setAutoCancel(true)
            notificationManager.notify(id.toInt(), notificationBuilder.build())
        } catch (e: Exception) {
            println("Notification error: " + e.stackTraceToString())
            Toast.makeText(context, "Notification error", Toast.LENGTH_LONG).show()
        }

    }

    @KoinApiExtension
    private fun newTimeAction(context: Context): PendingIntent {
        val newTimeIntent = Intent(context, NotificationReceiver::class.java).apply {
            this.action = ACTION_NEW_TIME
            this.putExtra(ACTION_NEW_TIME, true)
        }
        return PendingIntent.getBroadcast(
            context,
            1010,
            newTimeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun deleteAction(context: Context, id: Long): NotificationCompat.Action {
        val deleteIntent =
            Intent(context.applicationContext, Notification::class.java)
        deleteIntent.action = ACTION_DELETE
        deleteIntent.putExtra(NotificationRepository.INTENT_NOTE_ID, id)
        val deletePendingIntent = PendingIntent.getService(
            context.applicationContext,
            1111,
            deleteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Action.Builder(
            R.drawable.baseline_delete_20,
            "Delete",
            deletePendingIntent
        ).build()
    }

//    private fun deleteAction(context: Context, id: Long): PendingIntent {
//        val deleteIntent = Intent(context.applicationContext, ActionService::class.java)
//        deleteIntent.action = ACTION_DELETE
//        deleteIntent.putExtra(NotificationRepository.INTENT_NOTE_ID, id)
//        return PendingIntent.getBroadcast(
//            context.applicationContext,
//            1011,
//            deleteIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//    }

    fun hideNotification(context: Context) {
        try {
            val intent = Intent(context, NotificationReceiver::class.java)
            val cancelIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(cancelIntent)
        } catch (e: Exception) {
            println("Notification error: " + e.stackTraceToString())
            Toast.makeText(context, "Notification error", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(notificationManager: NotificationManager, context: Context) {
        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                context.getString(NOTIFICATION_CHANNEL_NAME),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID
        const val NOTIFICATION_CHANNEL_NAME = R.string.app_name
        const val ACTION_DELETE = "delete"
        const val ACTION_NEW_TIME = "newTime"
    }
}

