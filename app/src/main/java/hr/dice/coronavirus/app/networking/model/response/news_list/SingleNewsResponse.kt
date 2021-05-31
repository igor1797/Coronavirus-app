package hr.dice.coronavirus.app.networking.model.response.news_list

import com.google.gson.annotations.SerializedName
import hr.dice.coronavirus.app.model.news_list.SingleNews
import hr.dice.coronavirus.app.networking.base.DomainMapper

data class SingleNewsResponse(
    val category: String,
    val description: String,
    val image: String?,
    @SerializedName("published_at")
    val publishedAt: String,
    val source: String,
    val title: String,
    val url: String
) : DomainMapper<SingleNews> {
    override fun mapToDomain(): SingleNews {
        return SingleNews(category, description, image, publishedAt, source, title, url)
    }
}
