package ru.practicum.android.diploma.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.search.domain.models.VacancyPreview

class VacanciesViewHolder(private val binding: VacancyItemBinding):
RecyclerView.ViewHolder(binding.root){

    fun bind(element: VacancyPreview){
        with(binding){
            veTvDescription.text = element.description
            veTvEmployer.text = element.employer
            veTvSalary.text = element.salary

            Glide.with(itemView)
                .load(element.iconUrl)
                .placeholder(R.drawable.ic_logo)
                .fitCenter()
                .into(veIvIcon)
        }
    }
}
