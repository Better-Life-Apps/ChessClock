package com.betterlifeapps.chessclock.ui.settings.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.databinding.ItemCustomBinding

class CustomListAdapter(private val clickListener: ListItemClickListener) :
    ListAdapter<ItemCustomTimeControl, CustomListAdapter.CustomViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CustomViewHolder(inflater.inflate(R.layout.item_custom, parent, false))
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        with(holder.binding) {
            val item = getItem(position)
            name.text = item.name
            date.text = item.date

            timeControlContainer.setOnClickListener {
                clickListener.onItemClicked(item.id)
            }
            timeControlContainer.setOnLongClickListener {
                clickListener.onItemLongClicked(item.id)
                true
            }
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemCustomBinding.bind(itemView)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemCustomTimeControl>() {
            override fun areItemsTheSame(
                oldItem: ItemCustomTimeControl,
                newItem: ItemCustomTimeControl
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ItemCustomTimeControl,
                newItem: ItemCustomTimeControl
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}