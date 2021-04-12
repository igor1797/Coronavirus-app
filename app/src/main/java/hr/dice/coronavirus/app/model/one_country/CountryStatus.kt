package hr.dice.coronavirus.app.model.one_country

import hr.dice.coronavirus.app.model.CasesStatus

data class CountryStatus(
    val name: String,
    val datesStatus: List<DateStatus>,
    val casesStatus: CasesStatus,
    val updated: String
)
