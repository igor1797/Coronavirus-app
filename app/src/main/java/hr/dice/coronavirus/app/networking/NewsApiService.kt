package hr.dice.coronavirus.app.networking

import hr.dice.coronavirus.app.networking.model.response.news_list.LatestNewsListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("news")
    suspend fun getLatestNewsList(
        @Query("access_key") accessKey: String,
        @Query("keywords") keywords: String,
        @Query("limit") perPageNews: Int,
        @Query("offset") page: Int
    ): Response<LatestNewsListResponse>
}
