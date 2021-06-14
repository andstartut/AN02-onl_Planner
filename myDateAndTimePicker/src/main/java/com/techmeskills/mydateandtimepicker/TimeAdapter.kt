package com.techmeskills.mydateandtimepicker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class TimeAdapter(
    private var items: List<Date>
) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        return TimeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.time_picker_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
       val listPosition = position % items.size
        holder.bind(items[listPosition], items.size > 12)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class TimeViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvTime = itemView.findViewById<TextView>(R.id.tvTime)

        fun bind(date: Date, isMinutes: Boolean) {
            if (isMinutes) {
                tvTime.text = minutesFormatter.format(date)
            }
            else {
                tvTime.text = hourFormatter.format(date)
            }
        }

        @SuppressLint("ConstantLocale")
        val hourFormatter = SimpleDateFormat("hh", Locale.getDefault())

        @SuppressLint("ConstantLocale")
        val minutesFormatter = SimpleDateFormat("mm", Locale.getDefault())
    }
}