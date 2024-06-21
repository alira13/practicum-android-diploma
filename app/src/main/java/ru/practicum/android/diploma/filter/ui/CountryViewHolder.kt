package ru.practicum.android.diploma.filter.ui

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ViewItemForwardBtnBinding
import ru.practicum.android.diploma.filter.domain.models.Region

class CountryViewHolder(
    private val binding: ViewItemForwardBtnBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Region) {
        binding.itemValueTv.text = model.name
    }
}
