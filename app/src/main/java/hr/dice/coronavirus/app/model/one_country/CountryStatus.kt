package hr.dice.coronavirus.app.model.one_country

import hr.dice.coronavirus.app.model.Case
import hr.dice.coronavirus.app.model.CasesStatus

data class CountryStatus(
    val name: String = "",
    val datesStatus: List<DateStatus> = emptyList(),
    val casesStatus: CasesStatus = CasesStatus(Case(0, 0), Case(0, 0), Case(0, 0), Case(0, 0)),
    val updated: String = ""
)
