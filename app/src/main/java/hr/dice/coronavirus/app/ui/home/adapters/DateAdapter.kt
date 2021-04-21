package hr.dice.coronavirus.app.ui.home.adapters

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.common.utils.formatDate
import hr.dice.coronavirus.app.common.utils.formatNumber
import hr.dice.coronavirus.app.databinding.DateOrStateItemBinding
import hr.dice.coronavirus.app.model.one_country.DateStatus
import android.view.LayoutInflater as LayoutInflater

class DateAdapter : RecyclerView.Adapter<DateAdapter.DateHolder>() {

    private val dates = arrayListOf<DateStatus>()

    fun setDates(dates: List<DateStatus>) {
        this.dates.clear()
        this.dates.addAll(dates)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = DataBindingUtil.inflate<DateOrStateItemBinding>(layoutInflater, R.layout.date_or_state_item, parent, false)
        return DateHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DateHolder, position: Int) {
        holder.bind(dates[position])
    }

    override fun getItemCount(): Int = dates.size

    inner class DateHolder(private val item: DateOrStateItemBinding) : RecyclerView.ViewHolder(item.root) {

        fun bind(dateStatus: DateStatus) {
            with(item) {
                stateNameOrDateText.text = formatDate(dateStatus.date)
                numberOfConfirmedCases.text = formatNumber(dateStatus.confirmed)
                numberOfActiveCases.text = formatNumber(dateStatus.active)
                numberOfDeceasedCases.text = formatNumber(dateStatus.deceased)
                numberOfRecoveredCases.text = formatNumber(dateStatus.recovered)
            }
        }
    }
}
