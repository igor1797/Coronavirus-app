package hr.dice.coronavirus.app.ui.latest_news_list.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationListener(
    private val linearLayoutManager: LinearLayoutManager,
    private val prefetchDistance: Int,
    private val newsPerPage: Int
) : RecyclerView.OnScrollListener() {

    private var currentOffset = 0
    private var previousTotalItemCount = 0
    private var loading = true

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val totalItemCount = linearLayoutManager.itemCount
        val lastVisibleItemPosition: Int = linearLayoutManager.findLastVisibleItemPosition()

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }
        if (!loading && lastVisibleItemPosition + prefetchDistance >= totalItemCount) {
            currentOffset += newsPerPage
            onLoadMoreLatestNews(currentOffset)
            loading = true
        }
    }

    abstract fun onLoadMoreLatestNews(newOffset: Int)
}
