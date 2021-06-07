package hr.dice.coronavirus.app.model.maps

import hr.dice.coronavirus.app.model.CasesStatus

data class MapsSelection(
    val casesStatus: CasesStatus,
    val name: String
)
