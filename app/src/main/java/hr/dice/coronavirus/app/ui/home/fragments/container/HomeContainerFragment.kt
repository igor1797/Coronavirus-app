package hr.dice.coronavirus.app.ui.home.fragments.container

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.common.gone
import hr.dice.coronavirus.app.common.visible
import hr.dice.coronavirus.app.databinding.FragmentHomeContainerBinding
import hr.dice.coronavirus.app.ui.base.BaseFragment
import hr.dice.coronavirus.app.ui.home.fragments.container.presentation.HomeContainerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeContainerFragment : BaseFragment<FragmentHomeContainerBinding>() {

    private val homeContainerViewModel: HomeContainerViewModel by viewModel()

    override val layoutResourceId: Int get() = R.layout.fragment_home_container

    override fun onPostViewCreated() {
        setBottomNav()
        observe()
    }

    private fun setBottomNav() {
        val navController: NavController = Navigation.findNavController(requireView().findViewById(R.id.bottomNavHostFragment))
        binding.bottomNav.apply {
            setupWithNavController(navController)
            setOnNavigationItemReselectedListener { }
        }
    }

    private fun observe() {
        with(homeContainerViewModel) {
            mapsItemEnabled.observe(viewLifecycleOwner) { mapsItemEnabled ->
                binding.bottomNav.menu.findItem(R.id.mapsFragment).isEnabled = mapsItemEnabled
            }
            bottomNavVisible.observe(viewLifecycleOwner) { bottomNavVisible ->
                if (bottomNavVisible) binding.bottomNav.visible() else binding.bottomNav.gone()
            }
        }
    }
}
