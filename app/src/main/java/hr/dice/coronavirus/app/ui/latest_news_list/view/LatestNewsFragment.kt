package hr.dice.coronavirus.app.ui.latest_news_list.view

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.common.visible
import hr.dice.coronavirus.app.databinding.FragmentLatestNewsBinding
import hr.dice.coronavirus.app.ui.base.BaseFragment
import hr.dice.coronavirus.app.ui.latest_news_list.adapter.NewsAdapter
import hr.dice.coronavirus.app.ui.latest_news_list.adapter.PaginationListener
import hr.dice.coronavirus.app.ui.latest_news_list.presentation.LatestNewsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LatestNewsFragment : BaseFragment<FragmentLatestNewsBinding>() {

    private val latestNewsViewModel by viewModel<LatestNewsViewModel>()
    private val newsAdapter by lazy { NewsAdapter { onSingleNewsClicked(it) } }
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var paginationListener: RecyclerView.OnScrollListener

    override val layoutResourceId: Int get() = R.layout.fragment_latest_news

    override fun onPostViewCreated() {
        binding.viewModel = latestNewsViewModel
        initViewModelObservers()
        setupRecycler()
    }

    private fun setupRecycler() {
        linearLayoutManager = LinearLayoutManager(context)
        paginationListener = object : PaginationListener(linearLayoutManager, LatestNewsViewModel.PREFETCH_DISTANCE, LatestNewsViewModel.NEWS_PER_PAGE) {
            override fun onLoadMoreLatestNews(newOffset: Int) {
                latestNewsViewModel.loadMoreLatestNews(newOffset)
            }
        }
        binding.latestNewsRecycler.apply {
            adapter = newsAdapter
            layoutManager = linearLayoutManager
            addOnScrollListener(paginationListener)
        }
    }

    private fun initViewModelObservers() {
        with(latestNewsViewModel) {
            isInitialNewsLoad.observe(viewLifecycleOwner) { isInitialNewsLoad ->
                if (isInitialNewsLoad) {
                    binding.success.visible()
                    val viewGroup = binding.root as ViewGroup
                    viewGroup.removeView(binding.loading)
                }
            }
            newsList.observe(viewLifecycleOwner) {
                newsAdapter.apply {
                    submitList(it)
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun onSingleNewsClicked(url: String) {
    }

    override fun onDestroyView() {
        binding.latestNewsRecycler.removeOnScrollListener(paginationListener)
        super.onDestroyView()
    }
}
