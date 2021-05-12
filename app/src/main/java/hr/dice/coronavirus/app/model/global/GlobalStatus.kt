package hr.dice.coronavirus.app.model.global

import hr.dice.coronavirus.app.model.CasesStatus

data class GlobalStatus(
    val countries: List<GlobalCountry>,
    val casesStatus: CasesStatus,
    val updated: String
) {
    var topThreeCountriesByConfirmedCases = emptyList<GlobalCountry>()
        private set

    fun setTopThreeCountriesByConfirmedCases(topThreeCountriesByConfirmedCases: List<GlobalCountry>) {
        this.topThreeCountriesByConfirmedCases = topThreeCountriesByConfirmedCases
    }
}
