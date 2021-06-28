package hr.dice.coronavirus.app.model

data class CasesStatus(
    val confirmed: Case,
    val active: Case,
    val recovered: Case,
    val deceased: Case
)
