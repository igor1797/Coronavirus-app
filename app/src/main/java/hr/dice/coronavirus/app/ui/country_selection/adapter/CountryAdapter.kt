package hr.dice.coronavirus.app.ui.country_selection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.databinding.CountryItemBinding
import hr.dice.coronavirus.app.model.country_list.Country
import hr.dice.coronavirus.app.ui.base.BaseHolder

class CountryAdapter(
    private val onItemSelected: (String) -> Unit
) : ListAdapter<Country, CountryAdapter.CountryHolder>(CountryDiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<CountryItemBinding>(inflater, R.layout.country_item, parent, false)
        return CountryHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {
        getItem(position)?.let { country ->
            holder.bindItem(country)
        }
    }

    inner class CountryHolder(private val binding: CountryItemBinding) : BaseHolder<Country, CountryItemBinding>(binding) {
        override fun bindItem(item: Country) {
            with(binding) {
                country = item
                root.setOnClickListener { onItemSelected(item.slug) }
            }
        }
    }

    companion object {
        private val CountryDiffItemCallback = object : DiffUtil.ItemCallback<Country>() {
            override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
                return oldItem == newItem
            }
        }
    }
}
