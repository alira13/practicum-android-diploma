package ru.practicum.android.diploma.filter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ViewItemIndustryBinding
import ru.practicum.android.diploma.filter.domain.models.Industry

class IndustryAdapter(
    private val clickListener: (industry: Industry) -> Unit
) : RecyclerView.Adapter<IndustryViewHolder>() {

    var industries = mutableListOf<Industry>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IndustryViewHolder(ViewItemIndustryBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount(): Int = industries.size

    fun setItems(newItems: List<Industry>) {
        industries = newItems.toMutableList()
        update()
    }

    fun update() {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val industry = industries[position]
        holder.bind(
            industry = industry
        )
        holder.itemView.setOnClickListener {
            clickListener.invoke(industry)
        }
    }
}
