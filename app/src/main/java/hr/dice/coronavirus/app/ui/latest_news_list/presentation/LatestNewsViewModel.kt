package hr.dice.coronavirus.app.ui.latest_news_list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import hr.dice.coronavirus.app.common.DATE_TIME_FORMAT
import hr.dice.coronavirus.app.common.KEYWORD_CORONA
import hr.dice.coronavirus.app.common.TIME_ZONE
import hr.dice.coronavirus.app.common.utils.DateTimeUtil
import hr.dice.coronavirus.app.model.news_list.LatestNewsList
import hr.dice.coronavirus.app.model.news_list.SingleNews
import hr.dice.coronavirus.app.repositories.NewsRepository
import hr.dice.coronavirus.app.ui.base.ViewState
import hr.dice.coronavirus.app.ui.base.onSuccess
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class LatestNewsViewModel(
    private val newsRepository: NewsRepository,
    private val dateTimeUtil: DateTimeUtil
) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    private val _isInitialNewsLoad = MutableLiveData(false)
    val isInitialNewsLoad: LiveData<Boolean> get() = _isInitialNewsLoad

    private val _offset = MutableLiveData<Int>()
    private var totalNews = 0

    private val news = arrayListOf<SingleNews>()

    val newsList: LiveData<List<SingleNews>> = _offset.switchMap { offset ->
        liveData {
            newsRepository.getLatestNewsList(KEYWORD_CORONA, NEWS_PER_PAGE, offset)
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
                        news.addAll(latestNews.newsList)
                        emit(news as List<SingleNews>)
                    }
                }
                .collect {
                    _viewState.postValue(it)
                }
        }
    }

    init {
        _offset.value = INITIAL_OFFSET
    }

    fun loadMoreLatestNews(newOffset: Int) {
        if (newOffset < totalNews) {
            _offset.value = newOffset
        }
    }

    companion object {
        private const val INITIAL_OFFSET = 0
        const val NEWS_PER_PAGE = 25
        const val PREFETCH_DISTANCE = 8
    }
}
