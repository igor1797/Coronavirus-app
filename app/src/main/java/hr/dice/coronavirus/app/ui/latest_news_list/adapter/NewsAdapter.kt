package hr.dice.coronavirus.app.ui.latest_news_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.databinding.SingleNewsItemBinding
import hr.dice.coronavirus.app.model.news_list.SingleNews
import hr.dice.coronavirus.app.ui.base.BaseHolder

class NewsAdapter(
    private val onItemSelected: (String) -> Unit
) : ListAdapter<SingleNews, NewsAdapter.SingleNewsAdapterHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleNewsAdapterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<SingleNewsItemBinding>(inflater, R.layout.single_news_item, parent, false)
        return SingleNewsAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleNewsAdapterHolder, position: Int) {
        getItem(position)?.let { singleNews ->
            holder.bindItem(singleNews)
        }
    }

    inner class SingleNewsAdapterHolder(private val binding: SingleNewsItemBinding) : BaseHolder<SingleNews, SingleNewsItemBinding>(binding) {
        override fun bindItem(item: SingleNews) {
            with(binding) {
                singleNews = item
                root.setOnClickListener { onItemSelected(item.url) }
            }
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<SingleNews>() {
            override fun areItemsTheSame(oldItem: SingleNews, newItem: SingleNews): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: SingleNews, newItem: SingleNews): Boolean {
                return oldItem == newItem
            }
        }
    }
}
