package com.betterlifeapps.chessclock.ui.settings.standard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.databinding.ItemStandardBinding

class StandardListAdapter :
    ListAdapter<ItemStandardTimeControl, StandardListAdapter.StandardViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StandardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStandardBinding.inflate(inflater, parent, false)
        return StandardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StandardViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            title.text = item.title
            timeDescription.text = item.timeDescription
            additionDescription.text = item.additionDescription

            val backgroundColorId = if (item.isSelected) R.color.yellow else R.color.white
            root.setCardBackgroundColor(ContextCompat.getColor(root.context, backgroundColorId))

            val primaryColorId = if (item.isSelected) R.color.white else R.color.gray_5
            val primaryColor = ContextCompat.getColor(root.context, primaryColorId)
            title.setTextColor(primaryColor)
            divider.setBackgroundColor(primaryColor)
            timeDescription.setTextColor(primaryColor)
            additionDescription.setTextColor(primaryColor)
        }
    }

    class StandardViewHolder(val binding: ItemStandardBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemStandardTimeControl>() {
            override fun areItemsTheSame(
                oldItem: ItemStandardTimeControl,
                newItem: ItemStandardTimeControl
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ItemStandardTimeControl,
                newItem: ItemStandardTimeControl
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}