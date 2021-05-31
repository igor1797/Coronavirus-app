package hr.dice.coronavirus.app.networking.model.response.news_list

import com.google.gson.annotations.SerializedName
import hr.dice.coronavirus.app.model.news_list.LatestNewsList
import hr.dice.coronavirus.app.networking.base.DomainMapper

data class LatestNewsListResponse(
    @SerializedName("data")
    val newsList: List<SingleNewsResponse>,
    val pagination: PaginationResponse
) : DomainMapper<LatestNewsList> {
    override fun mapToDomain(): LatestNewsList {
        return LatestNewsList(newsList.map { it.mapToDomain() }, pagination.mapToDomain())
    }
}
