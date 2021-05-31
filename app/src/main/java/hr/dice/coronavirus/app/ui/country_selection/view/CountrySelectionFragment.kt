package hr.dice.coronavirus.app.ui.country_selection.view

import androidx.recyclerview.widget.LinearLayoutManager
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.common.gone
import hr.dice.coronavirus.app.common.visible
import hr.dice.coronavirus.app.databinding.CountrySelectionFragmentBinding
import hr.dice.coronavirus.app.ui.base.BaseFragment
import hr.dice.coronavirus.app.ui.country_selection.adapter.CountryAdapter
import hr.dice.coronavirus.app.ui.country_selection.presentation.CountrySelectionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountrySelectionFragment : BaseFragment<CountrySelectionFragmentBinding>() {

    private val countrySelectionViewModel by viewModel<CountrySelectionViewModel>()
    private val countryAdapter by lazy { CountryAdapter { onItemSelected(it) } }

    override val layoutResourceId: Int get() = R.layout.country_selection_fragment

    override fun onPostViewCreated() {
        binding.apply {
            viewModel = countrySelectionViewModel
            countrySelectionFragment = this@CountrySelectionFragment
        }
        setupRecycler()
        initViewModelObservers()
    }

    private fun setupRecycler() {
        binding.countryListRecycler.apply {
            adapter = countryAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initViewModelObservers() {
        with(countrySelectionViewModel) {
            filteredCountryList.observe(viewLifecycleOwner) {
                countryAdapter.submitList(it)
                with(binding) {
                    if (it.isEmpty()) {
                        countryListRecycler.gone()
                        noResultsFound.visible()
                    } else {
                        countryListRecycler.visible()
                        noResultsFound.gone()
                    }
                }
            }
            successfulSavedUserSelection.observe(viewLifecycleOwner) {
                if (it) goBack()
            }
        }
    }

    private fun onItemSelected(selection: String) {
        countrySelectionViewModel.saveUserSelection(selection)
    }

    fun goBack() {
        navigateBack()
    }
}
