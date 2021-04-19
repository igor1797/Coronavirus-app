package hr.dice.coronavirus.app.repositories

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
import kotlinx.coroutines.flow.flow

class CoronavirusRepository(
    private val coronavirusApiService: CoronavirusApiService
) : BaseRepository() {

    fun getGlobalStatusData() = fetchData { coronavirusApiService.getGlobalData() }

    fun getDayOneAllStatusByCountry(country: String) = flow {
        emit(Loading)
        makeNetworkRequest {
            coronavirusApiService.getDayOneAllStatusByCountry(country)
        }.onSuccess<List<OneCountryStatusResponse>> { list ->
            emit(Success(mapListToCountryStatus(list)))
        }.onNoInternetConnection {
            emit(NoInternetState)
        }.onFailure { emit(Error(it)) }
    }

    private fun mapListToCountryStatus(list: List<OneCountryStatusResponse>): CountryStatus {
        val latestDate = list.last()
        val dayBeforeLatestDate = list[list.size - 2]
        val activeCases = Case(latestDate.active, latestDate.active - dayBeforeLatestDate.active)
        val confirmedCases = Case(latestDate.confirmed, latestDate.confirmed - dayBeforeLatestDate.confirmed)
        val recoveredCases = Case(latestDate.recovered, latestDate.recovered - dayBeforeLatestDate.recovered)
        val deceasedCases = Case(latestDate.deaths, latestDate.deaths - dayBeforeLatestDate.deaths)
        val casesStatus = CasesStatus(confirmedCases, activeCases, recoveredCases, deceasedCases)
        val datesStatus = list.map { it.mapToDomain() }.reversed()
        return CountryStatus(latestDate.country, datesStatus, casesStatus, latestDate.date)
    }
}
