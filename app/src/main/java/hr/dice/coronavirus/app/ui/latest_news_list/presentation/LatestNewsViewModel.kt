package hr.dice.coronavirus.app.ui.latest_news_list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import hr.dice.coronavirus.app.common.DATE_TIME_FORMAT
import hr.dice.coronavirus.app.common.KEYWORD_CORONA
import hr.dice.coronavirus.app.common.TIME_ZONE
import hr.dice.coronavirus.app.common.utils.DateTimeUtil
import hr.dice.coronavirus.app.model.news_list.LatestNewsList
import hr.dice.coronavirus.app.model.news_list.SingleNews
import hr.dice.coronavirus.app.repositories.NewsRepository
import hr.dice.coronavirus.app.ui.base.ViewState
import hr.dice.coronavirus.app.ui.base.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LatestNewsViewModel(
    private val newsRepository: NewsRepository,
    private val dateTimeUtil: DateTimeUtil
) : ViewModel() {

    private val _isInitialNewsLoad = MutableLiveData(false)
    val isInitialNewsLoad: LiveData<Boolean> get() = _isInitialNewsLoad

    private val _offset = MutableSharedFlow<Int>(1)
    private var totalNews = 0

    val viewState: LiveData<ViewState> = _offset.flatMapLatest { offset ->
        fetchMoreLatestNews(offset)
    }.asLiveData(viewModelScope.coroutineContext)

    private val news = MutableStateFlow<List<SingleNews>>(emptyList())
    val newsList: LiveData<List<SingleNews>> = news.asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
            _offset.emit(INITIAL_OFFSET)
        }
    }

    private fun fetchMoreLatestNews(offset: Int) = newsRepository.getLatestNewsList(KEYWORD_CORONA, NEWS_PER_PAGE, offset)
        .map {
            it.onSuccess<LatestNewsList> { latestNews ->
                if (latestNews.pagination.offset == INITIAL_OFFSET) {
                    _isInitialNewsLoad.value = true
                    totalNews = latestNews.pagination.total
                }
                latestNews.newsList.forEach { singleNews ->
                    singleNews.setTimeAgo(
                        dateTimeUtil.getTimeAgo(
                            DATE_TIME_FORMAT,
                            TIME_ZONE,
                            singleNews.publishedAt
                        )
                    )
                }
                val newsList = news.value.toMutableList()
                newsList.addAll(latestNews.newsList)
                news.value = newsList
            }
        }

    fun loadMoreLatestNews(newOffset: Int) {
        if (newOffset < totalNews) {
            viewModelScope.launch {
                _offset.emit(newOffset)
            }
        }
    }

    companion object {
        private const val INITIAL_OFFSET = 0
        const val NEWS_PER_PAGE = 25
        const val PREFETCH_DISTANCE = 8
    }
}
