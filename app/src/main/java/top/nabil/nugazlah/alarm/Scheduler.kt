package top.nabil.nugazlah.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class Scheduler(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    // TODO alarm scheduled only when user receive the task, make it realtime with websocket
    @SuppressLint("ScheduleExactAlarm")
    fun schedule(data: AlarmData, triggerAt: Long) {
        val intent = Intent(context, AlarmNotifReceiver::class.java).apply {
            putExtra("ID", data.id)
            putExtra("SUBJECT", data.subject)
            putExtra("TITLE", data.title)
            putExtra("DESCRIPTION", data.description)
            putExtra("DEADLINE", data.deadline)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            data.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            pendingIntent
        )
    }


    private fun parseDateTime(dateString: String, timeString: String): LocalDateTime {
        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.getDefault())
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val localDate = dateFormatter.parse(dateString, LocalDate::from)

        val localTime = LocalTime.parse(timeString, timeFormatter)

        return LocalDateTime.of(localDate, localTime)
    }
}
