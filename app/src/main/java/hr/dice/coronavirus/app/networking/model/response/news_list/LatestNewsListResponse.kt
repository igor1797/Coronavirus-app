package hr.dice.coronavirus.app.networking.model.response.news_list

import com.google.gson.annotations.SerializedName

data class LatestNewsListResponse(
    @SerializedName("data")
    val newsList: List<SingleNewsResponse>,
    val pagination: Pagination
)
