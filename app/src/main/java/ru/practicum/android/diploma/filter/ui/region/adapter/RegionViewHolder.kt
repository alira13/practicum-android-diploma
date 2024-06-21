package ru.practicum.android.diploma.filter.ui.region.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ViewItemForwardBtnBinding
import ru.practicum.android.diploma.filter.domain.models.Area

class RegionViewHolder(
    private val binding: ViewItemForwardBtnBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(element: Area) {
        with(binding) {
            itemValueTv.text = element.name
        }
    }
}
