package hr.dice.coronavirus.app.ui.base

sealed class UseCase
object WorldWide : UseCase()
object CountrySelected : UseCase()
