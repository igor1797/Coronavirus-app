package hr.dice.coronavirus.app.common.utils

import android.app.Application
import hr.dice.coronavirus.app.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import java.util.concurrent.TimeUnit

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
        val timeAgoInMiliseconds = currentTimeInMillis - convertTimeInMillis(dateTimeFormat, timeZone, time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeAgoInMiliseconds)
        val days = TimeUnit.MILLISECONDS.toDays(timeAgoInMiliseconds)
        val months = days / ONE_MONTH_IN_DAYS
        val years = months / ONE_YEAR_IN_MONTHS
        return when {
            years == 1L -> context.getString(R.string.oneYearAgoText)
            years > 1L -> context.getString(R.string.yearsAgoText, years.toString())
            months == 1L -> context.getString(R.string.oneMonthAgoText)
            months > 1L -> context.getString(R.string.monthsAgoText, months.toString())
            days == 1L -> context.getString(R.string.oneDayAgoText)
            days > 1L -> context.getString(R.string.daysAgoText, days.toString())
            minutes == 1L -> context.getString(R.string.oneMinuteAgoText)
            minutes > 1L -> context.getString(R.string.minsAgoText, minutes.toString())
            else -> context.getString(R.string.nowText)
        }
    }

    companion object {
        private const val ONE_MONTH_IN_DAYS = 30
        private const val ONE_YEAR_IN_MONTHS = 12
    }
}
