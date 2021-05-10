package hr.dice.coronavirus.app.ui.country_selection.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import hr.dice.coronavirus.app.model.country_list.Country
import hr.dice.coronavirus.app.repositories.CountryRepository
import hr.dice.coronavirus.app.ui.base.ViewState
import hr.dice.coronavirus.app.ui.base.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class CountrySelectionViewModel(
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val _successfulSavedUserSelection = MutableLiveData(false)
    val successfulSavedUserSelection: LiveData<Boolean> get() = _successfulSavedUserSelection

    private val _searchQuery = MutableLiveData<String>()

    private val _countryList = MutableLiveData<ViewState>()
    val countryList: LiveData<ViewState> get() = _countryList

    private val unfilteredCountryList = arrayListOf<Country>()

    val filteredCountryList: LiveData<List<Country>> = _searchQuery.switchMap { searchQuery ->
        liveData {
            if (searchQuery.isEmpty()) {
                emit(unfilteredCountryList as List<Country>)
            } else {
                val filteredCountriesByQuery = withContext(Dispatchers.Default) {
                    unfilteredCountryList.filter { it.name.toLowerCase(Locale.getDefault()).contains(searchQuery.toLowerCase(Locale.getDefault())) }
                }
                emit(filteredCountriesByQuery)
            }
        }
    }

    init {
        getCountryList()
    }

    fun getCountryList() {
        viewModelScope.launch {
            countryRepository.getCountryList().collect {
                _countryList.postValue(it)
                it.onSuccess<List<Country>> { countries ->
                    val sortedCountriesByName = withContext(Dispatchers.Default) {
                        countries.sortedBy { country -> country.name }
                    }
                    unfilteredCountryList.addAll(sortedCountriesByName)
                    _searchQuery.value = ""
                }
            }
        }
    }

    fun searchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun saveUserSelection(selection: String) {
        viewModelScope.launch {
            countryRepository.saveUserSelection(selection)
            _successfulSavedUserSelection.value = true
        }
    }
}
