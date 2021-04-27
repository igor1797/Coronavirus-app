package hr.dice.coronavirus.app.common.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class DateTimeUtil(
    private val simpleDateFormat: SimpleDateFormat,
    private val calendar: Calendar
) {

    private fun convertTimeInMillis(
        dateTimeFormat: String,
        timeZone: String,
        time: String
    ): Long {
        simpleDateFormat.applyLocalizedPattern(dateTimeFormat)
        simpleDateFormat.timeZone = TimeZone.getTimeZone(timeZone)
        val timeWithTimeZone = "$time $timeZone"
        val date: Date? = simpleDateFormat.parse(timeWithTimeZone)
        return date?.time ?: 0L
    }

    fun getTimeAgo(dateTimeFormat: String, timeZone: String, time: String): String {
        val currentTimeInMillis = calendar.timeInMillis
        val timeAgoInSeconds = ((currentTimeInMillis - convertTimeInMillis(dateTimeFormat, timeZone, time)) / 1000)
        return when (timeAgoInSeconds) {
            in 0..59 -> "now"
            in 60..119 -> "1 min ago"
            in 119..3599 -> "${timeAgoInSeconds / 60} mins ago"
            in 3600..7199 -> "1 hour ago"
            in 7200..86399 -> "${timeAgoInSeconds / 3600} hours ago"
            in 86400..172799 -> "1 day ago"
            in 172800..2629742 -> "${timeAgoInSeconds / 86400} days ago"
            in 2629743..5259486 -> "1 month ago"
            in 5259487..31556926 -> "${timeAgoInSeconds / 2629743} months ago"
            in 31556926..63113851 -> "1 year ago"
            else -> "${timeAgoInSeconds / 31556926} years ago"
        }
    }
}
