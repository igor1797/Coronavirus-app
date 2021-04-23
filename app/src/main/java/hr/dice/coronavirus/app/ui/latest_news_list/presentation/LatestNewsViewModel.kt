package hr.dice.coronavirus.app.ui.latest_news_list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.dice.coronavirus.app.common.KEYWORD_CORONA
import hr.dice.coronavirus.app.repositories.NewsRepository
import hr.dice.coronavirus.app.ui.base.ViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LatestNewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _newsList = MutableLiveData<ViewState>()
    val newsList: LiveData<ViewState> get() = _newsList

    init {
        viewModelScope.launch {
            newsRepository.getLatestNewsList(KEYWORD_CORONA, 25, 0).collect {
                _newsList.postValue(it)
            }
        }
    }
}
