package hr.dice.coronavirus.app.ui.home.fragments.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import hr.dice.coronavirus.app.common.WORLDWIDE
import hr.dice.coronavirus.app.model.global.GlobalCountry
import hr.dice.coronavirus.app.model.global.GlobalStatus
import hr.dice.coronavirus.app.repositories.CoronavirusRepository
import hr.dice.coronavirus.app.repositories.CountryRepository
import hr.dice.coronavirus.app.repositories.ResourceRepository
import hr.dice.coronavirus.app.ui.base.CountrySelected
import hr.dice.coronavirus.app.ui.base.UseCase
import hr.dice.coronavirus.app.ui.base.ViewState
import hr.dice.coronavirus.app.ui.base.WorldWide
import hr.dice.coronavirus.app.ui.base.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val coronavirusRepository: CoronavirusRepository,
    private val countryRepository: CountryRepository,
    private val resourceRepository: ResourceRepository
) : ViewModel() {

    private var timeAgo = 0

    private val _useCase = MutableSharedFlow<UseCase>(1)
    val useCase: LiveData<UseCase> = _useCase.asLiveData(viewModelScope.coroutineContext)

    init {
        getStatisticsData()
    }

    val coronaDataStatus: LiveData<ViewState> = _useCase.flatMapLatest { useCase ->
        Log.d("TAGER", ": aaaaaaaaaa")
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
        }
    }.asLiveData(viewModelScope.coroutineContext)

    fun getStatisticsData() {
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
                    resourceRepository.getNowText()
                }
                in ONE_MINUTE_IN_SECONDS..ONE_HOUR_IN_SECONDS -> {
                    resourceRepository.getMinutesTimeAgoText(timeAgo / ONE_MINUTE_IN_SECONDS)
                }
                else -> {
                    resourceRepository.getHoursTimeAgoText(timeAgo / ONE_HOUR_IN_SECONDS)
                }
            }
            emit(time)
            delay(ONE_SECOND_IN_MILISECONDS)
        }
    }

    private fun setUseCase(useCase: UseCase) {
        viewModelScope.launch {
            _useCase.emit(useCase)
        }
    }

    private fun findTopThreeCountriesByConfirmedCases(countries: List<GlobalCountry>): List<GlobalCountry> {
        return countries.sortedBy { it.confirmedCases }.asReversed().take(3)
    }

    companion object {
        private const val ONE_MINUTE_IN_SECONDS = 60
        private const val ONE_HOUR_IN_SECONDS = 3600
        private const val ONE_SECOND_IN_MILISECONDS = 1000L
    }
}
