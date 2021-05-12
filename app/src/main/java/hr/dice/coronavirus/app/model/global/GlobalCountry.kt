package hr.dice.coronavirus.app.model.global

data class GlobalCountry(
    val name: String,
    val slug: String,
    val confirmedCases: Int,
    val activeCases: Int,
    val recoveredCases: Int,
    val deathCases: Int
)
