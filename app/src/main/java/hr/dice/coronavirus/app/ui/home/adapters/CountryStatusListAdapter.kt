package hr.dice.coronavirus.app.ui.home.adapters

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.common.utils.formatNumber
import hr.dice.coronavirus.app.databinding.DateOrStateItemBinding
import hr.dice.coronavirus.app.model.global.GlobalCountry
import android.view.LayoutInflater as LayoutInflater

class CountryStatusListAdapter : ListAdapter<GlobalCountry, CountryStatusListAdapter.StateHolder>(GlobalCountryDiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = DataBindingUtil.inflate<DateOrStateItemBinding>(layoutInflater, R.layout.date_or_state_item, parent, false)
        return StateHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: StateHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class StateHolder(private val item: DateOrStateItemBinding) : RecyclerView.ViewHolder(item.root) {

        fun bind(stateStatus: GlobalCountry) {
            with(item) {
                stateNameOrDateText.text = stateStatus.name
                numberOfConfirmedCases.text = formatNumber(stateStatus.confirmedCases)
                numberOfActiveCases.text = formatNumber(stateStatus.activeCases)
                numberOfDeceasedCases.text = formatNumber(stateStatus.deathCases)
                numberOfRecoveredCases.text = formatNumber(stateStatus.recoveredCases)
            }
        }
    }

    companion object {
        private val GlobalCountryDiffItemCallback = object : DiffUtil.ItemCallback<GlobalCountry>() {
            override fun areItemsTheSame(oldItem: GlobalCountry, newItem: GlobalCountry): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: GlobalCountry, newItem: GlobalCountry): Boolean {
                return oldItem == newItem
            }
        }
    }
}
