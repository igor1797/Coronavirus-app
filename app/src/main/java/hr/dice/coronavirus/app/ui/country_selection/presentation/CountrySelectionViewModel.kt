package hr.dice.coronavirus.app.ui.country_selection.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import hr.dice.coronavirus.app.model.country_list.Country
import hr.dice.coronavirus.app.repositories.CountryRepository
import hr.dice.coronavirus.app.ui.base.Success
import hr.dice.coronavirus.app.ui.base.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class CountrySelectionViewModel(
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val _swipeRefreshIsVisible = MutableLiveData(false)
    val swipeRefreshIsVisible: LiveData<Boolean> get() = _swipeRefreshIsVisible

    private val _successfulSavedUserSelection = MutableLiveData(false)
    val successfulSavedUserSelection: LiveData<Boolean> get() = _successfulSavedUserSelection

    val searchQuery = MutableLiveData<String>()

    private val _countryList = MutableLiveData<ViewState>()
    val countryList: LiveData<ViewState> get() = _countryList

    private val unfilteredCountryList = arrayListOf<Country>()

    val filteredCountryList: LiveData<List<Country>> = searchQuery.switchMap { searchQuery ->
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
        getCountryListData()
    }

    fun getCountryListData() {
        viewModelScope.launch {
            countryRepository.getCountryList().collect {
                _countryList.postValue(it)
                if (it is Success<*>) {
                    _swipeRefreshIsVisible.value = false
                    val countries = it.data as List<Country>
                    val sortedCountriesByName = withContext(Dispatchers.Default) {
                        countries.sortedBy { country -> country.name }
                    }
                    unfilteredCountryList.addAll(sortedCountriesByName)
                    searchQuery.value = ""
                } else {
                    _swipeRefreshIsVisible.value = true
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
