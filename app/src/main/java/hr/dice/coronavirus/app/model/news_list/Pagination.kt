package hr.dice.coronavirus.app.model.news_list

data class Pagination(
    val count: Int,
    val newsPerPage: Int,
    val page: Int,
    val total: Int
)
