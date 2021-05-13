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


class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(context: Context?, intent: Intent?) {
        showNotification(context!!, intent)
    }

    private fun showNotification(context: Context, intent: Intent?) {
        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel(notificationManager, context)
            }
            val text = intent?.getStringExtra(NotificationRepository.INTENT_NOTE_TITLE)
            val title = context.getString(R.string.app_name)

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
                .addAction(R.drawable.baseline_delete_20, "I did it", deleteAction(context))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
            notificationManager.notify(0, notificationBuilder.build())
        } catch (e: Exception) {
            println("Notification error: " + e.stackTraceToString())
            Toast.makeText(context, "Notification error", Toast.LENGTH_LONG).show()
        }

    }

    private fun deleteAction(context: Context): PendingIntent {
        val deleteIntent = Intent(context, NotificationReceiver::class.java)
        deleteIntent.action = ACTION_DELETE
        deleteIntent.putExtra(ACTION_DELETE, true)
        return PendingIntent.getBroadcast(
            context,
            0,
            deleteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

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
    }
}

