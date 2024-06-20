package ru.practicum.android.diploma.filter.ui.region.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ViewItemForwardBtnBinding
import ru.practicum.android.diploma.filter.domain.models.Region

class RegionViewHolder(
    private val binding: ViewItemForwardBtnBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(element: Region) {
        with(binding) {
            itemValueTv.text = element.name
        }
    }
}
