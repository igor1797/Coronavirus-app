package hr.dice.coronavirus.app.model.global

import hr.dice.coronavirus.app.model.CasesStatus

data class GlobalStatus(
    var countries: List<GlobalCountry>,
    val casesStatus: CasesStatus,
    val updated: String
)
