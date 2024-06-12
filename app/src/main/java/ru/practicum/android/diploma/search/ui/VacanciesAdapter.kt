package ru.practicum.android.diploma.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.search.domain.models.VacancyPreview

class VacanciesAdapter(
    private var onItemClickListener: ((VacancyPreview) -> Unit)
): RecyclerView.Adapter<VacanciesViewHolder>() {

    lateinit var vacancies: List<VacancyPreview>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacanciesViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return VacanciesViewHolder(VacancyItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount() = vacancies.size

    override fun onBindViewHolder(holder: VacanciesViewHolder, position: Int) {
        holder.bind(vacancies[position])
        holder.itemView.setOnClickListener { _ ->
            onItemClickListener.invoke(vacancies[holder.adapterPosition])
        }
    }
}
