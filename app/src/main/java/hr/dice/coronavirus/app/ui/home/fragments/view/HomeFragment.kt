package hr.dice.coronavirus.app.ui.home.fragments.view

import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.dice.coronavirus.app.R
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
import hr.dice.coronavirus.app.ui.home.fragments.presentation.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel by viewModel<HomeViewModel>()
    private val dateAdapter by lazy { DateAdapter() }
    private val stateAdapter by lazy { StateAdapter() }

    override val layoutResourceId: Int get() = R.layout.fragment_home

    override fun onPostViewCreated() {
        binding.viewModel = viewModel
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
        with(viewModel) {
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
        viewModel.getTimeAgo().observe(viewLifecycleOwner) {
            binding.updated.text = getString(R.string.lastUpdatedText, it)
        }
    }

    private fun handleCountrySelectedSuccess(countryStatus: CountryStatus) {
        if (countryStatus.datesStatus.isEmpty()) {
            navigateToCountrySelection()
            showErrorDialog()
        } else {
            dateAdapter.setDates(countryStatus.datesStatus)
            with(binding) {
                casesStatus = countryStatus.casesStatus
                selection.text = countryStatus.name
            }
        }
    }

    private fun handleWorldwideSelectedSuccess(globalStatus: GlobalStatus) {
        stateAdapter.setTopThreeStatesByConfirmedCases(globalStatus.topThreeCountriesByConfirmedCases)
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
