package hr.dice.coronavirus.app.repositories

import hr.dice.coronavirus.app.datastore.DataStoreSelectionManager
import hr.dice.coronavirus.app.model.country_list.Country
import hr.dice.coronavirus.app.networking.CountryApiService
import hr.dice.coronavirus.app.networking.base.onFailure
import hr.dice.coronavirus.app.networking.base.onNoInternetConnection
import hr.dice.coronavirus.app.networking.base.onSuccess
import hr.dice.coronavirus.app.networking.model.response.country_list.CountryResponse
import hr.dice.coronavirus.app.repositories.base.BaseRepository
import hr.dice.coronavirus.app.ui.base.Error
import hr.dice.coronavirus.app.ui.base.Loading
import hr.dice.coronavirus.app.ui.base.NoInternetState
import hr.dice.coronavirus.app.ui.base.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class CountryRepository(
    private val countryApiService: CountryApiService,
    private val dataStoreSelectionManager: DataStoreSelectionManager
) : BaseRepository() {

    private val _countries = MutableStateFlow(listOf<Country>())
    val countries: StateFlow<List<Country>> get() = _countries

    fun getCountryList() = flow {
        emit(Loading)
        makeNetworkRequest {
            countryApiService.getCountryList()
        }.onSuccess<List<CountryResponse>> { countries ->
            val mappedCountries = mapCountryListToDomain(countries)
            _countries.value = mappedCountries
            emit(Success(mappedCountries))
        }.onNoInternetConnection {
            emit(NoInternetState)
        }.onFailure {
            emit(Error(it))
        }
    }

    suspend fun saveUserSelection(selection: String) {
        dataStoreSelectionManager.storeUserSelection(selection)
    }

    fun getUserSelection() = dataStoreSelectionManager.userSelection

    private fun mapCountryListToDomain(countries: List<CountryResponse>) = countries.map { it.mapToDomain() }
}
