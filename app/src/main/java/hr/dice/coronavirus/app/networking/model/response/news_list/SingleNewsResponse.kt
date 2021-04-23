package hr.dice.coronavirus.app.networking.model.response.news_list

data class SingleNewsResponse(
    val category: String,
    val description: String,
    val image: String?,
    val published_at: String,
    val source: String,
    val title: String,
    val url: String
)
