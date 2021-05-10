package hr.dice.coronavirus.app.ui.home.fragments.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import hr.dice.coronavirus.app.common.WORLDWIDE
import hr.dice.coronavirus.app.model.global.GlobalCountry
import hr.dice.coronavirus.app.model.global.GlobalStatus
import hr.dice.coronavirus.app.repositories.CoronavirusRepository
import hr.dice.coronavirus.app.repositories.CountryRepository
import hr.dice.coronavirus.app.ui.base.CountrySelected
import hr.dice.coronavirus.app.ui.base.UseCase
import hr.dice.coronavirus.app.ui.base.ViewState
import hr.dice.coronavirus.app.ui.base.WorldWide
import hr.dice.coronavirus.app.ui.base.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val coronavirusRepository: CoronavirusRepository,
    private val countryRepository: CountryRepository
) : ViewModel() {

    private var timeAgo: Int = 0

    private val _useCase = MutableLiveData<UseCase>()
    val useCase: LiveData<UseCase> get() = _useCase

    init {
       getStatisticsData()
    }

    val coronaDataStatus: LiveData<ViewState> = useCase.switchMap { useCase ->
        liveData {
            timeAgo = 0
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
            }.collect {
                emit(it)
            }
        }
    }

    fun getStatisticsData(){
        viewModelScope.launch {
            countryRepository.getUserSelection().collect { userSelection ->
                if (userSelection.isEmpty() || userSelection == WORLDWIDE) {
                    setUseCase(WorldWide)
                } else {
                    setUseCase(CountrySelected(userSelection))
                }
            }
        }
    }

    fun getTimeAgo() = liveData {
        while (true) {
            timeAgo++
            val time = when (timeAgo) {
                in 0..59 -> {
                    "now"
                }
                in ONE_MINUTE_IN_SECONDS..ONE_HOUR_IN_SECONDS -> {
                    "${timeAgo / ONE_MINUTE_IN_SECONDS}m ago"
                }
                else -> {
                    "${timeAgo / ONE_HOUR_IN_SECONDS}h ago"
                }
            }
            emit(time)
            delay(ONE_SECOND_IN_MILISECONDS)
        }
    }

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

    companion object {
        private const val ONE_MINUTE_IN_SECONDS = 60
        private const val ONE_HOUR_IN_SECONDS = 3600
        private const val ONE_SECOND_IN_MILISECONDS = 1000L
    }
}
