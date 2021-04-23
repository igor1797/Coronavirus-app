package hr.dice.coronavirus.app.ui.latest_news_list.view

import androidx.recyclerview.widget.LinearLayoutManager
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.databinding.FragmentLatestNewsBinding
import hr.dice.coronavirus.app.model.news_list.LatestNewsList
import hr.dice.coronavirus.app.ui.base.BaseFragment
import hr.dice.coronavirus.app.ui.base.onSuccess
import hr.dice.coronavirus.app.ui.latest_news_list.adapter.NewsAdapter
import hr.dice.coronavirus.app.ui.latest_news_list.presentation.LatestNewsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LatestNewsFragment : BaseFragment<FragmentLatestNewsBinding>() {

    private val latestNewsViewModel by viewModel<LatestNewsViewModel>()
    private val newsAdapter by lazy { NewsAdapter { onSingleNewsClicked(it) } }

    override val layoutResourceId: Int get() = R.layout.fragment_latest_news

    override fun onPostViewCreated() {
        binding.viewModel = latestNewsViewModel
        observe()
        setupRecycler()
    }

    private fun setupRecycler() {
        binding.latestNewsRecycler.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observe() {
        latestNewsViewModel.newsList.observe(viewLifecycleOwner) {
            it.onSuccess<LatestNewsList> { newsListResponse ->
                newsAdapter.submitList(newsListResponse.newsList)
            }
        }
    }

    private fun onSingleNewsClicked(url: String) {
    }
}
