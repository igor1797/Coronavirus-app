package hr.dice.coronavirus.app.ui.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import hr.dice.coronavirus.app.model.global.GlobalCountry
import hr.dice.coronavirus.app.model.global.GlobalStatus
import hr.dice.coronavirus.app.repositories.CoronavirusRepository
import hr.dice.coronavirus.app.ui.base.CountrySelected
import hr.dice.coronavirus.app.ui.base.UseCase
import hr.dice.coronavirus.app.ui.base.ViewState
import hr.dice.coronavirus.app.ui.base.WorldWide
import hr.dice.coronavirus.app.ui.base.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val coronavirusRepository: CoronavirusRepository,
    initialUseCase: UseCase
) : ViewModel() {

    private val _useCase = MutableStateFlow(initialUseCase)
    val useCase: LiveData<UseCase> get() = _useCase.asLiveData(viewModelScope.coroutineContext)

    val coronaDataStatus: LiveData<ViewState> = _useCase.flatMapLatest { useCase ->
        when (useCase) {
            is CountrySelected -> {
                coronavirusRepository.getDayOneAllStatusByCountry(useCase.country)
            }
            WorldWide -> {
                coronavirusRepository.getGlobalStatusData()
                    .map {
                        it.onSuccess<GlobalStatus> { globalStatus ->
                            globalStatus.setTopThreeCountriesByConfirmedCases(
                                withContext(Dispatchers.Default) {
                                    findTopThreeCountriesByConfirmedCases(globalStatus.countries)
                                }
                            )
                        }
                    }
            }
        }
    }.asLiveData(viewModelScope.coroutineContext)

    private fun setUseCase(useCase: UseCase) {
        _useCase.value = useCase
    }

    private fun findTopThreeCountriesByConfirmedCases(countries: List<GlobalCountry>): List<GlobalCountry> {
        val topThreeCountriesByConfirmedCases = arrayListOf<GlobalCountry>()
        var countryWithHighestConfirmedCases = GlobalCountry()
        var secondCountryWithHighestConfirmedCases = GlobalCountry()
        var thirdCountryWithHighestConfirmedCases = GlobalCountry()
        countries.forEach { country ->
            when {
                country.confirmedCases > countryWithHighestConfirmedCases.confirmedCases -> {
                    thirdCountryWithHighestConfirmedCases = secondCountryWithHighestConfirmedCases
                    secondCountryWithHighestConfirmedCases = countryWithHighestConfirmedCases
                    countryWithHighestConfirmedCases = country
                }
                country.confirmedCases > secondCountryWithHighestConfirmedCases.confirmedCases -> {
                    thirdCountryWithHighestConfirmedCases = secondCountryWithHighestConfirmedCases
                    secondCountryWithHighestConfirmedCases = country
                }
                country.confirmedCases > thirdCountryWithHighestConfirmedCases.confirmedCases ->
                    thirdCountryWithHighestConfirmedCases = country
            }
        }
        topThreeCountriesByConfirmedCases.apply {
            add(countryWithHighestConfirmedCases)
            add(secondCountryWithHighestConfirmedCases)
            add(thirdCountryWithHighestConfirmedCases)
        }
        return topThreeCountriesByConfirmedCases
    }
}
