package top.nabil.nugazlah.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import top.nabil.nugazlah.R

class AlarmNotifReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val id = intent?.getStringExtra("ID") ?: ""
        val subject = intent?.getStringExtra("SUBJECT") ?: ""
        val title = intent?.getStringExtra("TITLE") ?: ""
        val description = intent?.getStringExtra("DESCRIPTION") ?: ""
        val deadline = intent?.getStringExtra("DEADLINE") ?: ""

        val notification = NotificationCompat.Builder(
            context,
            NOTIFICANTION_CHANNEL_ID,
        )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(context.getString(R.string.notif_title, subject, deadline))
            .setContentText(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .build()

        notificationManager.notify(id.toIntOrNull() ?: 1, notification)
    }
}