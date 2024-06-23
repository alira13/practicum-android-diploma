package ru.practicum.android.diploma.filter.ui.adapters

import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ViewItemIndustryBinding
import ru.practicum.android.diploma.filter.domain.models.Industry

class IndustryViewHolder(
    private val binding: ViewItemIndustryBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(industry: Industry) {
        binding.apply {
            industryName.text = industry.name
            if (industry.isChecked) {
                radioButton.setImageDrawable(
                    AppCompatResources.getDrawable(
                        itemView.context,
                        R.drawable.ic_radio_button_on
                    )
                )
            } else {
                radioButton.setImageDrawable(
                    AppCompatResources.getDrawable(
                        itemView.context,
                        R.drawable.ic_radio_button_off
                    )
                )
            }
        }
    }
}
