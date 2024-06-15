package ru.practicum.android.diploma.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.practicum.android.diploma.databinding.ProgressbarItemBinding
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.search.domain.models.ProgressBarItem
import ru.practicum.android.diploma.search.domain.models.VacancyPreview
import ru.practicum.android.diploma.search.ui.models.ViewType

class VacanciesAdapter(
    private var onItemClickListener: ((VacancyPreview) -> Unit)
) : RecyclerView.Adapter<ViewHolder>() {

    var vacancies = mutableListOf<ViewType>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return when (viewType) {
            VACANCY_ITEM -> VacanciesViewHolder(VacancyItemBinding.inflate(layoutInspector, parent, false))
            PROGRESSBAR_ITEM -> ProgressBarViewHolder(ProgressbarItemBinding.inflate(layoutInspector, parent, false))
            else -> throw IllegalStateException()
        }
    }

    override fun getItemCount() = vacancies.size

    override fun getItemViewType(position: Int): Int {
        return when (vacancies[position]) {
            is VacancyPreview -> VACANCY_ITEM
            is ProgressBarItem -> PROGRESSBAR_ITEM
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is VacanciesViewHolder -> {
                holder.bind(vacancies[position] as VacancyPreview)
                holder.itemView.setOnClickListener { _ ->
                    onItemClickListener.invoke(vacancies[holder.adapterPosition] as VacancyPreview)
                }
            }
            is ProgressBarViewHolder -> {
                holder.itemView
            }
        }
    }

    companion object {
        const val VACANCY_ITEM = 1
        const val PROGRESSBAR_ITEM = 2
    }
}
