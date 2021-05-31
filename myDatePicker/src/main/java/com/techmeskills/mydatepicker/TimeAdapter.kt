package com.techmeskills.mydatepicker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class TimeAdapter(
        private val items: List<Date>
) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {
    var selectedHour: Date? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        return TimeViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.date_picker_view, parent, false)
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
        fun bind(date: Date) {
            tvHour.text = monthFormatter.format(date)
        }

        companion object {
            @SuppressLint("ConstantLocale")
            val monthFormatter = SimpleDateFormat("hh", Locale.getDefault())
        }
    }

}