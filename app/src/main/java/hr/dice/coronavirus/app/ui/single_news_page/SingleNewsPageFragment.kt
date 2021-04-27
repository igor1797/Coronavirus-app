package hr.dice.coronavirus.app.ui.single_news_page

import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.databinding.FragmentSingleNewsPageBinding
import hr.dice.coronavirus.app.ui.base.BaseFragment

class SingleNewsPageFragment : BaseFragment<FragmentSingleNewsPageBinding>() {

    private val args: SingleNewsPageFragmentArgs by navArgs()

    override val layoutResourceId: Int get() = R.layout.fragment_single_news_page

    override fun onPostViewCreated() {
        initListeners()
        loadWebViewContent()
    }

    private fun loadWebViewContent() {
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(args.singleNewsUrl)
        }
    }

    private fun initListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
