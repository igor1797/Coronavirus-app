package hr.dice.coronavirus.app.networking.model.response.news_list

data class Pagination(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val total: Int
)
