package hr.dice.coronavirus.app.repositories

import hr.dice.coronavirus.app.common.ACCESS_KEY
import hr.dice.coronavirus.app.networking.NewsApiService
import hr.dice.coronavirus.app.repositories.base.BaseRepository

class NewsRepository(
    private val newsApiService: NewsApiService
) : BaseRepository() {

    fun getLatestNewsList(keywords: String, newsPerPage: Int, page: Int) = fetchData {
        newsApiService.getLatestNewsList(ACCESS_KEY, keywords, newsPerPage, page)
    }
}
