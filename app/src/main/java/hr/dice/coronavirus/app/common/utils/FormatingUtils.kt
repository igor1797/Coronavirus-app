package hr.dice.coronavirus.app.common.utils

import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.absoluteValue

fun formatNumber(number: Int): String {
    return when {
        number < -999999 -> "-${("%.1f".format(number.absoluteValue / 1000000.0))}M"
        number > -1000000 && number < -9999 -> "-${("%.1f".format(number.absoluteValue / 1000.0))}K"
        number < 9999 -> "$number"
        number < 999999 -> "${("%.1f".format(number / 1000.0))}K"
        else -> "${("%.1f".format(number / 1000000.0))}M"
    }
}

fun formatDate(date: String): String {
    val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    val localedDate = LocalDate.parse(date, formatter)
    return "${localedDate.dayOfMonth}.${localedDate.monthValue}.${localedDate.year}"
}

fun formatNumberWithCommaSeparate(number: Int): String {
    return NumberFormat.getNumberInstance(Locale.getDefault()).format(number)
}
