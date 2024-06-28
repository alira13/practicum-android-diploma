package ru.practicum.android.diploma.filter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ViewItemForwardBtnBinding
import ru.practicum.android.diploma.filter.domain.models.Region

class CountryAdapter : RecyclerView.Adapter<CountryViewHolder>() {

    var onCountryClickListener: ((Region) -> Unit)? = null

    var countries = mutableListOf<Region>()
        set(value) {
            var otherRegionIndex: Int? = null
            for (i in value.indices) {
                if (value[i].name == OTHER_REGION) {
                    otherRegionIndex = i
                    break
                }
            }

            if (otherRegionIndex != null) {
                val otherRegion = value.removeAt(otherRegionIndex)
                value.add(otherRegion)
            }
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ViewItemForwardBtnBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val item = countries[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onCountryClickListener?.invoke(item)
        }
    }

    override fun getItemCount() = countries.size

    companion object {
        private const val OTHER_REGION = "Другие регионы"
    }
}
