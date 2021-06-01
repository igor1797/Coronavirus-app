package hr.dice.coronavirus.app.ui.country_selection.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import hr.dice.coronavirus.app.model.country_list.Country
import hr.dice.coronavirus.app.repositories.CountryRepository
import hr.dice.coronavirus.app.ui.base.ViewState
import hr.dice.coronavirus.app.ui.base.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.Locale

class CountrySelectionViewModel(
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val _successfulSavedUserSelection = MutableLiveData(false)
    val successfulSavedUserSelection: LiveData<Boolean> get() = _successfulSavedUserSelection

    val searchQuery = MutableLiveData("")
    private val _searchQuery = searchQuery.asFlow()

    private val _countryList = MutableLiveData<ViewState>()
    val countryList: LiveData<ViewState> get() = _countryList

    private val countries = MutableStateFlow(listOf<Country>())

    val filteredCountryList: LiveData<List<Country>> =
        _searchQuery.combineTransform(countries) { searchQuery, countries ->
            if (searchQuery.isEmpty()) {
                emit(countries.sortedBy { it.name })
            } else {
                emit(filterCountries(countries, searchQuery))
            }
        }.flowOn(Dispatchers.Default).asLiveData(viewModelScope.coroutineContext)

    init {
        getCountryListData()
    }

    private fun filterCountries(countries: List<Country>, searchQuery: String): List<Country> {
        return countries.filter {
            it.name.toLowerCase(Locale.getDefault()).contains(searchQuery.toLowerCase(Locale.getDefault()))
        }.sortedBy { it.name }
    }

    fun getCountryListData() {
        viewModelScope.launch {
            countryRepository.getCountryList().collect {
                _countryList.postValue(it)
                it.onSuccess<List<Country>> { countryList ->
                    countries.value = countryList
                }
            }
        }
    }

    fun saveUserSelection(selection: String) {
        viewModelScope.launch {
            countryRepository.saveUserSelection(selection)
            _successfulSavedUserSelection.value = true
        }
    }
}
