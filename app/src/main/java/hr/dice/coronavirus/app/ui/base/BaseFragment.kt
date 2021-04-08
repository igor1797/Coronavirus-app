package hr.dice.coronavirus.app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import hr.dice.coronavirus.app.common.gone
import hr.dice.coronavirus.app.common.visible

abstract class BaseFragment<viewBinding : ViewDataBinding> : Fragment() {

    protected abstract val layoutResourceId: Int
    protected lateinit var binding: viewBinding private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    abstract fun setUpUi()

    protected fun showLoading(errorView: View, successView: View, loadingView: ProgressBar, noInternetView: View) {
        loadingView.visible()
        errorView.gone()
        successView.gone()
        noInternetView.gone()
    }

    protected fun showData(errorView: View, successView: View, loadingView: ProgressBar, noInternetView: View) {
        successView.visible()
        errorView.gone()
        loadingView.gone()
        noInternetView.gone()
    }

    protected fun showError(errorView: View, successView: View, loadingView: ProgressBar, noInternetView: View) {
        errorView.visible()
        successView.gone()
        loadingView.gone()
        noInternetView.gone()
    }

    protected fun showNoInternet(errorView: View, successView: View, loadingView: ProgressBar, noInternetView: View) {
        noInternetView.visible()
        errorView.gone()
        successView.gone()
        loadingView.gone()
    }
}
