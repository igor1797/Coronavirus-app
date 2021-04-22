package hr.dice.coronavirus.app.ui.country_selection.view

import androidx.navigation.fragment.findNavController
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.databinding.CountrySelectionFragmentBinding
import hr.dice.coronavirus.app.ui.base.BaseFragment

class CountrySelectionFragment : BaseFragment<CountrySelectionFragmentBinding>() {

    override val layoutResourceId: Int get() = R.layout.country_selection_fragment

    override fun onPostViewCreated() {
        setBackButtonOnClickListener()
    }

    private fun setBackButtonOnClickListener(){
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}