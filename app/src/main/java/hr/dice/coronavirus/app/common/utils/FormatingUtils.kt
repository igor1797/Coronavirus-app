package hr.dice.coronavirus.app.common.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun formatNumber(number: Int): String {
    return when {
        number < 9999 -> "$number"
        number < 999999 -> "${("%.2f".format(number / 1000.0))}K"
        else -> "${("%.2f".format(number / 1000000.0))}M"
    }
}

fun formatDate(date: String): String {
    val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    val localedDate = LocalDate.parse(date, formatter)
    return "${localedDate.dayOfMonth}.${localedDate.monthValue}.${localedDate.year}"
}
