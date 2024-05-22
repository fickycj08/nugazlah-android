package top.nabil.nugazlah.util

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ParseTime {
    companion object {
        fun iso8601ToReadable(deadlineStr: String): String {
            val isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")
            val deadline = LocalDateTime.parse(deadlineStr, isoFormatter)
            val targetFormatter =
                DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy HH:mm:ss", Locale("id"))
            return deadline.atZone(ZoneId.of("Asia/Jakarta"))
                .withZoneSameInstant(ZoneId.systemDefault())
                .format(targetFormatter)
        }

        @OptIn(ExperimentalMaterial3Api::class)
        fun formatDateToString(datePicked: DatePickerState): String {
            val selectedDate = Calendar.getInstance().apply {
                timeInMillis = datePicked.selectedDateMillis!!
            }

            val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
            return dateFormat.format(selectedDate.time)
        }

        @OptIn(ExperimentalMaterial3Api::class)
        fun formatTimeToString(time: TimePickerState): String {
            val hour = time.hour.toString().padStart(2, '0')
            val minute = time.minute.toString().padStart(2, '0')
            val second = "00"
            return "$hour:$minute:$second"
        }

        fun dateTimeStringToIso8601(date: String, time: String): String {
            val dateTimeFormat = SimpleDateFormat("EEEE, d MMMM yyyy HH:mm:ss", Locale.getDefault())
            val iso8601Format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val dateTime = "$date $time"
            val parsedDate = dateTimeFormat.parse(dateTime)
            return iso8601Format.format(parsedDate!!)
        }
    }
}