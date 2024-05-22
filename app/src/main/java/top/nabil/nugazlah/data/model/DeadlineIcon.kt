package top.nabil.nugazlah.data.model

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

enum class DeadlineType {
    DONE,
    SANS,
    WARN,
    NINUNINU,
    MISSED,
    UNKNOWN,
}

val deadlineIcon = mapOf(
    Pair(DeadlineType.DONE, "✅"),
    Pair(DeadlineType.SANS, "\uD83D\uDE0E"),
    Pair(DeadlineType.WARN, "\uD83D\uDE36\u200D\uD83C\uDF2B\uFE0F"),
    Pair(DeadlineType.NINUNINU, "⛔"),
    Pair(DeadlineType.MISSED, "☠\uFE0F"),
    Pair(DeadlineType.UNKNOWN, "❔"),
)

fun determineDeadlineType(deadlineStr: String): DeadlineType {
    val isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")
    val deadline = LocalDateTime.parse(deadlineStr, isoFormatter).atZone(ZoneId.of("Asia/Jakarta"))
        .withZoneSameInstant(ZoneId.of("Asia/Jakarta")).toLocalDateTime()
    val now = LocalDateTime.now(ZoneId.of("Asia/Jakarta"))
    val hoursUntilDeadline = ChronoUnit.HOURS.between(now, deadline)
    return when {
        hoursUntilDeadline > 7 * 24 -> DeadlineType.SANS
        hoursUntilDeadline in 1 * 24..7 * 24 -> DeadlineType.WARN
        hoursUntilDeadline in 1..<1 * 24 -> DeadlineType.NINUNINU
        hoursUntilDeadline < 0 -> DeadlineType.MISSED
        else -> DeadlineType.UNKNOWN
    }
}
