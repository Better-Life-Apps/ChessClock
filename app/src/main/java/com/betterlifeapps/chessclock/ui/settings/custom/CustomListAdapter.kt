package com.betterlifeapps.chessclock.ui.settings.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.databinding.ItemCustomBinding

class CustomListAdapter(private val clickListener: ListItemClickListener) :
    ListAdapter<ItemCustomGameMode, CustomListAdapter.CustomViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CustomViewHolder(inflater.inflate(R.layout.item_custom, parent, false))
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        with(holder.binding) {
            val item = getItem(position)
            name.text = item.name
            date.text = item.date

            check.isVisible = item.isSelected

            timeControlContainer.setOnClickListener {
                clickListener.onItemClicked(item)
            }
            timeControlContainer.setOnLongClickListener {
                clickListener.onItemLongClicked(item)
                true
            }
        }
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemCustomBinding.bind(itemView)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemCustomGameMode>() {
            override fun areItemsTheSame(
                oldItem: ItemCustomGameMode,
                newItem: ItemCustomGameMode
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ItemCustomGameMode,
                newItem: ItemCustomGameMode
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}