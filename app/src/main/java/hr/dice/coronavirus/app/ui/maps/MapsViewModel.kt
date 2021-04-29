package hr.dice.coronavirus.app.ui.maps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import hr.dice.coronavirus.app.common.WORLDWIDE
import hr.dice.coronavirus.app.model.Case
import hr.dice.coronavirus.app.model.CasesStatus
import hr.dice.coronavirus.app.model.global.GlobalCountry
import hr.dice.coronavirus.app.model.global.GlobalStatus
import hr.dice.coronavirus.app.model.maps.MapsSelection

class MapsViewModel : ViewModel() {

    var topThreeCountriesByConfirmedStates: List<GlobalCountry>? = null
        private set
    var worldwide: GlobalStatus? = null
        private set

    private val _userSelection = MutableLiveData<String>()

    val selectedCountryOrWorldwide = _userSelection.switchMap { userSelection ->
        liveData {
            if (userSelection == WORLDWIDE) {
                worldwide?.let {
                    val mapsSelection = MapsSelection(it.casesStatus, "Worldwide")
                    emit(mapsSelection)
                }
            } else {
                topThreeCountriesByConfirmedStates?.let {
                    val country: GlobalCountry? = topThreeCountriesByConfirmedStates?.first { userSelection == it.name }
                    country?.let {
                        val casesStatus = CasesStatus(Case(it.confirmedCases), Case(it.activeCases), Case(it.recoveredCases), Case(it.deathCases))
                        val mapsSelection = MapsSelection(casesStatus, country.name)
                        emit(mapsSelection)
                    }
                }
            }
        }
    }

    fun setTopThreeCountriesByConfirmedStates(topThreeCountriesByConfirmedStates: List<GlobalCountry>) {
        this.topThreeCountriesByConfirmedStates = topThreeCountriesByConfirmedStates
    }

    fun setWorldwideGlobalStatus(globalStatus: GlobalStatus) {
        this.worldwide = globalStatus
    }

    fun changeUserSelection(userSelection: String) {
        _userSelection.value = userSelection
    }

    fun findIfCountryIsInTopThreeCountries(countryName: String) {
        val country = topThreeCountriesByConfirmedStates?.firstOrNull { countryName == it.name }
        country?.let {
            changeUserSelection(countryName)
        } ?: changeUserSelection(WORLDWIDE)
    }
}
