package hr.dice.coronavirus.app.repositories

import hr.dice.coronavirus.app.common.EMPTY_STRING
import hr.dice.coronavirus.app.model.Case
import hr.dice.coronavirus.app.model.CasesStatus
import hr.dice.coronavirus.app.model.one_country.CountryStatus
import hr.dice.coronavirus.app.networking.CoronavirusApiService
import hr.dice.coronavirus.app.networking.base.onFailure
import hr.dice.coronavirus.app.networking.base.onNoInternetConnection
import hr.dice.coronavirus.app.networking.base.onSuccess
import hr.dice.coronavirus.app.networking.model.response.one_country.OneCountryStatusResponse
import hr.dice.coronavirus.app.repositories.base.BaseRepository
import hr.dice.coronavirus.app.ui.base.Error
import hr.dice.coronavirus.app.ui.base.Loading
import hr.dice.coronavirus.app.ui.base.NoInternetState
import hr.dice.coronavirus.app.ui.base.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

private const val UNITED_STATES_OF_AMERICA_COUNTRY_SLUG = "united-states"

class CoronavirusRepository(
    private val coronavirusApiService: CoronavirusApiService
) : BaseRepository() {

    fun getGlobalStatusData() = fetchData { coronavirusApiService.getGlobalData() }

    fun getDayOneAllStatusByCountry(country: String) = flow {
        emit(Loading)
        if (country == UNITED_STATES_OF_AMERICA_COUNTRY_SLUG) {
            makeNetworkRequest {
                coronavirusApiService.getTotalStatusByCountry(country)
            }
        } else {
            makeNetworkRequest {
                coronavirusApiService.getDayOneAllStatusByCountry(country)
            }
        }.onSuccess<List<OneCountryStatusResponse>> { list ->
            if (list.isEmpty() || list.size == 1) {
                emit(Success(CountryStatus()))
            } else {
                emit(Success(mapListToCountryStatus(list)))
            }
        }.onNoInternetConnection {
            emit(NoInternetState)
        }.onFailure { emit(Error(it)) }
    }

    private suspend fun mapListToCountryStatus(list: List<OneCountryStatusResponse>): CountryStatus {
        val filteredCountryStatusList = withContext(Dispatchers.Default) {
            list.filter { it.province == EMPTY_STRING }
        }
        val latestDate = filteredCountryStatusList.last()
        val dayBeforeLatestDate = filteredCountryStatusList[filteredCountryStatusList.size - 2]
        val activeCases = Case(latestDate.active, latestDate.active - dayBeforeLatestDate.active)
        val confirmedCases = Case(latestDate.confirmed, latestDate.confirmed - dayBeforeLatestDate.confirmed)
        val recoveredCases = Case(latestDate.recovered, latestDate.recovered - dayBeforeLatestDate.recovered)
        val deceasedCases = Case(latestDate.deaths, latestDate.deaths - dayBeforeLatestDate.deaths)
        val casesStatus = CasesStatus(confirmedCases, activeCases, recoveredCases, deceasedCases)
        val datesStatus = filteredCountryStatusList.map { it.mapToDomain() }.reversed()
        return CountryStatus(latestDate.country, datesStatus, casesStatus, latestDate.date)
    }
}
