package hr.dice.coronavirus.app.model.global

data class GlobalCountry(
    val name: String = "",
    val slug: String = "",
    val confirmedCases: Int = 0,
    val activeCases: Int = 0,
    val recoveredCases: Int = 0,
    val deathCases: Int = 0
)
