package hr.dice.coronavirus.app.ui.home.fragments.container

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.databinding.FragmentHomeContainerBinding
import hr.dice.coronavirus.app.ui.base.BaseFragment

class HomeContainerFragment : BaseFragment<FragmentHomeContainerBinding>() {

    override val layoutResourceId: Int get() = R.layout.fragment_home_container

    override fun onPostViewCreated() {
        setBottomNav()
    }

    private fun setBottomNav() {
        val navController: NavController = Navigation.findNavController(requireView().findViewById(R.id.bottomNavHostFragment))
        binding.bottomNav.apply {
            setupWithNavController(navController)
            setOnNavigationItemReselectedListener { }
        }
    }
}
