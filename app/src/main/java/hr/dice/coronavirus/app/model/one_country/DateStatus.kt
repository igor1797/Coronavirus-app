package hr.dice.coronavirus.app.model.one_country

data class DateStatus(
    val date: String,
    val confirmed: Int,
    val active: Int,
    val recovered: Int,
    val deceased: Int
)
