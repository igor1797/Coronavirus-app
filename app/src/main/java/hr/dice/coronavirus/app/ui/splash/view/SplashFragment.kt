package hr.dice.coronavirus.app.ui.splash.view

import androidx.navigation.fragment.findNavController
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.databinding.SplashFragmentBinding
import hr.dice.coronavirus.app.ui.base.BaseFragment
import hr.dice.coronavirus.app.ui.splash.presentation.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<SplashFragmentBinding>() {

    private val viewModel: SplashViewModel by viewModel()

    override val layoutResourceId: Int get() = R.layout.splash_fragment

    override fun onPostViewCreated() {
        initViewModelObservers()
    }

    private fun initViewModelObservers() {
        viewModel.startHomeContainerScreen.observe(viewLifecycleOwner) { startHomeContainerScreen ->
            if (startHomeContainerScreen) navigateToHomeContainerScreen()
        }
    }

    private fun navigateToHomeContainerScreen() {
        findNavController().navigate(R.id.goToHomeContainerFragment)
    }
}
