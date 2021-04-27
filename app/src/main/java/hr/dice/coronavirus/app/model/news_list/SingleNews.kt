package hr.dice.coronavirus.app.model.news_list

data class SingleNews(
    val category: String,
    val description: String,
    val image: String?,
    val publishedAt: String,
    val source: String,
    val title: String,
    val url: String
) {
    var timeAgo = ""
        private set

    fun setTimeAgo(timeAgo: String) {
        this.timeAgo = timeAgo
    }
}
