package hr.dice.coronavirus.app.model.news_list

data class LatestNewsList(
    val newsList: List<SingleNews>,
    val pagination: Pagination
)
