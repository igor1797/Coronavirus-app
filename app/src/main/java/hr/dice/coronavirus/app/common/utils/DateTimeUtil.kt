package hr.dice.coronavirus.app.common.utils

import android.app.Application
import hr.dice.coronavirus.app.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class DateTimeUtil(
    private val simpleDateFormat: SimpleDateFormat,
    private val calendar: Calendar,
    private val context: Application
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
            in 0..59 -> context.getString(R.string.nowText)
            in 60..119 -> context.getString(R.string.oneMinuteAgoText)
            in 119..3599 -> context.getString(R.string.minsAgoText, (timeAgoInSeconds / 60).toString())
            in 3600..7199 -> context.getString(R.string.oneHourAgoText)
            in 7200..86399 -> context.getString(R.string.hoursAgoText, (timeAgoInSeconds / 3600).toString())
            in 86400..172799 -> context.getString(R.string.oneDayAgoText)
            in 172800..2629742 -> context.getString(R.string.daysAgoText, (timeAgoInSeconds / 86400).toString())
            in 2629743..5259486 -> context.getString(R.string.oneMonthAgoText)
            in 5259487..31556926 -> context.getString(R.string.monthsAgoText, (timeAgoInSeconds / 2629743).toString())
            in 31556926..63113851 -> context.getString(R.string.oneYearAgoText)
            else -> context.getString(R.string.yearsAgoText, (timeAgoInSeconds / 31556926).toString())
        }
    }
}
