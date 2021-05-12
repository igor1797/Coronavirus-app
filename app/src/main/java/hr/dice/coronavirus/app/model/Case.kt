package hr.dice.coronavirus.app.model

/**
increasing function return 0 for not increase number of cases, 1 for increase and -1 for decrease
**/

data class Case(
    val total: Int,
    val new: Int
) {
    fun increasing(): Int {
        return when {
            new > 0 -> 1
            new < 0 -> -1
            else -> 0
        }
    }
}
