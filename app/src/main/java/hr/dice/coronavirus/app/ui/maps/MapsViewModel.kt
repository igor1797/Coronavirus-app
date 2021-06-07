package hr.dice.coronavirus.app.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import hr.dice.coronavirus.app.common.EMPTY_STRING
import hr.dice.coronavirus.app.common.WORLDWIDE
import hr.dice.coronavirus.app.model.Case
import hr.dice.coronavirus.app.model.CasesStatus
import hr.dice.coronavirus.app.model.global.GlobalCountry
import hr.dice.coronavirus.app.model.global.GlobalStatus
import hr.dice.coronavirus.app.model.maps.CountryLatLng
import hr.dice.coronavirus.app.model.maps.MapsSelection
import hr.dice.coronavirus.app.repositories.LocationRepository
import kotlinx.coroutines.launch

class MapsViewModel(
    private val locationRepository: LocationRepository
) : ViewModel() {

    var topThreeCountriesByConfirmedStates: List<GlobalCountry>? = null
        private set
    var worldwide: GlobalStatus? = null
        private set

    private val _countriesLatLng = MutableLiveData<List<CountryLatLng>?>()
    val countriesLatLng: LiveData<List<CountryLatLng>?> get() = _countriesLatLng

    private val _userSelection = MutableLiveData<String>()

    val mapsStatisticsData = _userSelection.switchMap { userSelection ->
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

    fun findIfCountryIsInTopThreeCountries(lat: Double, lng: Double) {
        viewModelScope.launch {
            val countryName = locationRepository.getFromLocation(lat, lng) ?: EMPTY_STRING
            val country = topThreeCountriesByConfirmedStates?.firstOrNull { countryName == it.name }
            country?.let {
                changeUserSelection(countryName)
            } ?: changeUserSelection(WORLDWIDE)
        }
    }

    fun getCountriesFromLocationName(name: String) {
        viewModelScope.launch {
            val countryLatLng = locationRepository.getFromLocationName(name)
            val countriesLatLng = arrayListOf<CountryLatLng>()
            countriesLatLng.apply {
                addAll(_countriesLatLng.value ?: emptyList())
                if (countryLatLng != null) {
                    add(countryLatLng)
                }
            }
            _countriesLatLng.postValue(countriesLatLng)
        }
    }
}
