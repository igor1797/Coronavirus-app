package hr.dice.coronavirus.app.ui.home.fragments.view

import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.common.sharedGraphViewModel
import hr.dice.coronavirus.app.databinding.FragmentHomeBinding
import hr.dice.coronavirus.app.model.global.GlobalStatus
import hr.dice.coronavirus.app.model.one_country.CountryStatus
import hr.dice.coronavirus.app.ui.activity.MainActivity
import hr.dice.coronavirus.app.ui.base.BaseFragment
import hr.dice.coronavirus.app.ui.base.CountrySelected
import hr.dice.coronavirus.app.ui.base.Success
import hr.dice.coronavirus.app.ui.base.UseCase
import hr.dice.coronavirus.app.ui.base.WorldWide
import hr.dice.coronavirus.app.ui.home.adapters.CountryStatusListAdapter
import hr.dice.coronavirus.app.ui.home.adapters.DateStatusListAdapter
import hr.dice.coronavirus.app.ui.home.fragments.presentation.HomeViewModel
import org.koin.core.parameter.parametersOf

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val dateStatusListAdapter by lazy { DateStatusListAdapter() }
    private val countryStatusListAdapter by lazy { CountryStatusListAdapter() }
    private val homeViewModel by sharedGraphViewModel<HomeViewModel>(R.id.home_container_graph) { parametersOf(WorldWide) }

    override val layoutResourceId: Int get() = R.layout.fragment_home

    override fun onPostViewCreated() {
        binding.apply {
            viewModel = homeViewModel
            homeFragment = this@HomeFragment
        }
        initViewModelObservers()
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            emptyState.backToSearch.setOnClickListener {
                navigateToCountrySelection()
            }
            error.tryAgain.setOnClickListener {
                tryAgainToFetchStatisticsData()
            }
            noInternetConnection.tryAgain.setOnClickListener {
                tryAgainToFetchStatisticsData()
            }
        }
    }

    private fun tryAgainToFetchStatisticsData() {
        homeViewModel.getStatisticsData()
    }

    fun navigateToCountrySelection() {
        val controller = Navigation.findNavController(activity as MainActivity, R.id.mainNavHostFragment)
        controller.navigate(R.id.goToCountrySelectionFragment)
    }

    private fun setupRecycler(dataAdapter: RecyclerView.Adapter<*>) {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dataAdapter
        }
    }

    private fun initViewModelObservers() {
        with(homeViewModel) {
            useCase.observe(viewLifecycleOwner) {
                when (it) {
                    is CountrySelected -> {
                        binding.apply {
                            setupRecycler(dateStatusListAdapter)
                            stateOrDateText.text = getText(R.string.dateText)
                        }
                    }
                    WorldWide -> {
                        binding.apply {
                            setupRecycler(countryStatusListAdapter)
                            stateOrDateText.text = getText(R.string.stateText)
                        }
                    }
                }
                coronaDataStatus.observe(viewLifecycleOwner) { viewState ->
                    if (viewState is Success<*>) {
                        handleSuccess(it, viewState)
                    }
                }
            }
        }
    }

    private fun handleSuccess(useCase: UseCase, successResponse: Success<*>) {
        when (useCase) {
            is CountrySelected -> handleCountrySelectedSuccess(successResponse.data as CountryStatus)
            WorldWide -> handleWorldwideSelectedSuccess(successResponse.data as GlobalStatus)
        }
        homeViewModel.getTimeAgo().observe(viewLifecycleOwner) {
            binding.updated.text = getString(R.string.lastUpdatedText, it)
        }
    }

    private fun handleCountrySelectedSuccess(countryStatus: CountryStatus) {
        dateStatusListAdapter.submitList(countryStatus.datesStatus)
        with(binding) {
            casesStatus = countryStatus.casesStatus
            selection.text = countryStatus.name
        }
    }

    private fun handleWorldwideSelectedSuccess(globalStatus: GlobalStatus) {
        countryStatusListAdapter.submitList(globalStatus.topThreeCountriesByConfirmedCases)
        with(binding) {
            casesStatus = globalStatus.casesStatus
            selection.text = getText(R.string.worldwideText)
        }
    }
}
