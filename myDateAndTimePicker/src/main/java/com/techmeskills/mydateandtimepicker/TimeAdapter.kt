package com.techmeskills.mydateandtimepicker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class TimeAdapter(
    private var items: List<Int>,
    private val setCurrentDate: Date?
) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        return TimeViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.time_picker_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class TimeViewHolder(
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvHour = itemView.findViewById<TextView>(R.id.tvTime)

        @SuppressLint("ResourceAsColor")
        fun bind(minute: Int) {
            val min = minute.toString().padStart(2, '0')
            tvHour.text = min
        }
    }

}