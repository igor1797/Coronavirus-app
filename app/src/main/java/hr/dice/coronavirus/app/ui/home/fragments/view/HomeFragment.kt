package hr.dice.coronavirus.app.ui.home.fragments.view

import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.common.sharedGraphViewModel
import hr.dice.coronavirus.app.databinding.FragmentHomeBinding
import hr.dice.coronavirus.app.model.global.GlobalStatus
import hr.dice.coronavirus.app.model.one_country.CountryStatus
import hr.dice.coronavirus.app.ui.base.BaseFragment
import hr.dice.coronavirus.app.ui.base.CountrySelected
import hr.dice.coronavirus.app.ui.base.Success
import hr.dice.coronavirus.app.ui.base.UseCase
import hr.dice.coronavirus.app.ui.base.WorldWide
import hr.dice.coronavirus.app.ui.home.activity.MainActivity
import hr.dice.coronavirus.app.ui.home.adapters.DateAdapter
import hr.dice.coronavirus.app.ui.home.adapters.StateAdapter
import hr.dice.coronavirus.app.ui.home.fragments.container.presentation.HomeContainerViewModel
import hr.dice.coronavirus.app.ui.home.fragments.presentation.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel by sharedGraphViewModel<HomeViewModel>(R.id.home_container_graph)
    private val homeContainerViewModel by lazy { requireParentFragment().requireParentFragment().getViewModel<HomeContainerViewModel>() }
    private val dateAdapter by lazy { DateAdapter() }
    private val stateAdapter by lazy { StateAdapter() }

    override val layoutResourceId: Int get() = R.layout.fragment_home

    override fun onPostViewCreated() {
        binding.viewModel = homeViewModel
        observe()
        setChangeSelectionOnClickLister()
    }

    private fun setChangeSelectionOnClickLister() {
        binding.changeSelection.setOnClickListener {
            navigateToCountrySelection()
        }
    }

    private fun navigateToCountrySelection() {
        val controller = Navigation.findNavController(activity as MainActivity, R.id.mainNavHostFragment)
        controller.navigate(R.id.goToCountrySelectionFragment)
    }

    private fun setupRecycler(dataAdapter: RecyclerView.Adapter<*>) {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dataAdapter
        }
    }

    private fun observe() {
        with(homeViewModel) {
            useCase.observe(viewLifecycleOwner) {
                when (it) {
                    is CountrySelected -> {
                        binding.apply {
                            setupRecycler(dateAdapter)
                            stateOrDateText.text = getText(R.string.dateText)
                        }
                    }
                    WorldWide -> {
                        binding.apply {
                            setupRecycler(stateAdapter)
                            stateOrDateText.text = getText(R.string.stateText)
                        }
                    }
                }
                coronaDataStatus.observe(viewLifecycleOwner) { viewState ->
                    if (viewState is Success<*>) {
                        handleSuccess(it, viewState)
                    } else {
                        homeContainerViewModel.disableMapsItem()
                    }
                }
            }
        }
    }

    private fun handleSuccess(useCase: UseCase, successResponse: Success<*>) {
        homeContainerViewModel.enableMapsItem()
        when (useCase) {
            is CountrySelected -> {
                val countryStatus = successResponse.data as CountryStatus
                dateAdapter.setDates(countryStatus.datesStatus)
                with(binding) {
                    casesStatus = countryStatus.casesStatus
                    selection.text = countryStatus.name
                }
            }
            WorldWide -> {
                val globalStatus = successResponse.data as GlobalStatus
                stateAdapter.setTopThreeStatesByConfirmedCases(globalStatus.countries)
                with(binding) {
                    casesStatus = globalStatus.casesStatus
                    selection.text = getText(R.string.worldwideText)
                }
            }
        }
        homeViewModel.getTimeAgo().observe(viewLifecycleOwner) {
            binding.updated.text = getString(R.string.lastUpdatedText, it)
        }
    }
}
