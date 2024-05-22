package top.nabil.nugazlah.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var taskId: String,
    var classId: String,
    var isAlarmRegistered: Boolean,
    var isTaskDone: Boolean = false,
)