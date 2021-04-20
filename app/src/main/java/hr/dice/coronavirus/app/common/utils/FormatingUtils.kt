package hr.dice.coronavirus.app.common.utils

fun formatNumber(number: Int): String {
    return when {
        number < 9999 -> "$number"
        number < 999999 -> "${("%.2f".format(number / 1000.0))}K"
        else -> "${("%.2f".format(number / 1000000.0))}M"
    }
}

fun formatDate(date: String): String {
    val dateAndTimeParts = date.split('T')
    val datePart = dateAndTimeParts.first()
    val dateParts = datePart.split('-')
    val year = dateParts.first()
    val month = dateParts[1]
    val day = dateParts.last()
    return "$day.$month.$year"
}
