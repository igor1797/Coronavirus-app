package hr.dice.coronavirus.app.ui.base

sealed class UseCase
object WorldWide : UseCase()
data class CountrySelected(val country: String) : UseCase()
