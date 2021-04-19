package hr.dice.coronavirus.app.model

import kotlin.math.absoluteValue

/**
increasing function return 0 for not increase number of cases, 1 for increase and -1 for decrease
**/

data class Case(
    val total: Int,
    val new: Int = 0
) {
    fun increasing(): Int {
        return when {
            new > 0 -> 1
            new < 0 -> -1
            else -> 0
        }
    }

    fun getNewNumberOfCasesFormatted(): String {
        return formatNumber(new.absoluteValue)
    }

    fun getTotalNumberOfCasesFormatted(): String {
        return formatNumber(total)
    }

    private fun formatNumber(number: Int): String {
        return when {
            number < 9999 -> "$number"
            number < 999999 -> "${("%.2f".format(number / 1000.0))}K"
            else -> "${("%.2f".format(number / 100000.0))}M"
        }
    }
}
