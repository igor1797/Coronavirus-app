package hr.dice.coronavirus.app.ui.country_selection.view

import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
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
    private val countryAdapter by lazy { CountryAdapter() }

    override val layoutResourceId: Int get() = R.layout.country_selection_fragment

    override fun onPostViewCreated() {
        binding.viewModel = countrySelectionViewModel
        setupRecycler()
        initListeners()
        observe()
    }

    private fun setupRecycler() {
        binding.countryListRecycler.apply {
            adapter = countryAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initListeners() {
        with(binding) {
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
            searchQueryInput.doOnTextChanged { text, _, _, _ ->
                text?.toString()?.let { searchQuery ->
                    countrySelectionViewModel.searchQueryChanged(searchQuery)
                }
            }
        }
    }

    private fun observe() {
        countrySelectionViewModel.filteredCountryList.observe(viewLifecycleOwner) {
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
    }
}
