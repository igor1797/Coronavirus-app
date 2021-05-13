package hr.dice.coronavirus.app.ui.home.fragments.view

import androidx.appcompat.app.AlertDialog
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
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.getViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val dateStatusListAdapter by lazy { DateStatusListAdapter() }
    private val countryStatusListAdapter by lazy { CountryStatusListAdapter() }
    private val homeViewModel by sharedGraphViewModel<HomeViewModel>(R.id.home_container_graph) { parametersOf(WorldWide) }
    private val homeContainerViewModel by lazy { requireParentFragment().requireParentFragment().getViewModel<HomeContainerViewModel>() }

    override val layoutResourceId: Int get() = R.layout.fragment_home

    override fun onPostViewCreated() {
        binding.viewModel = homeViewModel
        observe()
        initListeners()
    }

    private fun initListeners() {
        with(binding){
            changeSelection.setOnClickListener {
                navigateToCountrySelection()
            }
            noInternetConnection.tryAgain.setOnClickListener {
                tryAgainToFetchStatisticsData()
            }
            error.tryAgain.setOnClickListener {
                tryAgainToFetchStatisticsData()
            }
        }
    }

    private fun tryAgainToFetchStatisticsData(){
        homeViewModel.getStatisticsData()
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
            is CountrySelected -> handleCountrySelectedSuccess(successResponse.data as CountryStatus)
            WorldWide -> handleWorldwideSelectedSuccess(successResponse.data as GlobalStatus)
        }
        homeViewModel.getTimeAgo().observe(viewLifecycleOwner) {
            binding.updated.text = getString(R.string.lastUpdatedText, it)
        }
    }

    private fun handleCountrySelectedSuccess(countryStatus: CountryStatus) {
        if (countryStatus.datesStatus.isEmpty()) {
            navigateToCountrySelection()
            showErrorDialog()
        } else {
            dateStatusListAdapter.submitList(countryStatus.datesStatus)
            with(binding) {
                casesStatus = countryStatus.casesStatus
                selection.text = countryStatus.name
            }
        }
    }

    private fun handleWorldwideSelectedSuccess(globalStatus: GlobalStatus) {
        countryStatusListAdapter.submitList(globalStatus.topThreeCountriesByConfirmedCases)
        with(binding) {
            casesStatus = globalStatus.casesStatus
            selection.text = getText(R.string.worldwideText)
        }
    }

    private fun showErrorDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.noDataFourCountryTitleText)
                .setIcon(R.drawable.error)
                .setMessage(R.string.noDataFourCountryMessageText)
                .setPositiveButton(R.string.positiveAlertButtonText) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
}
