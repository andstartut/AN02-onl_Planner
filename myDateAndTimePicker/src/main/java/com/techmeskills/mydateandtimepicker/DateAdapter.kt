package com.techmeskills.mydateandtimepicker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter(
    private val items: List<Date>
) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {
    var selectedDay: Date? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.date_picker_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(items[position], selectedDay == items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DateViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvMonth = itemView.findViewById<TextView>(R.id.tvMonth)
        private val tvDay = itemView.findViewById<TextView>(R.id.tvDay)
        private val tvWeekDay = itemView.findViewById<TextView>(R.id.tvWeekDay)


        @SuppressLint("ResourceAsColor")
        fun bind(date: Date, selected: Boolean) {
            tvMonth.text = monthFormatter.format(date)
            tvDay.text = dayFormatter.format(date)
            tvWeekDay.text = weekDayFormatter.format(date)
        }

        companion object {
            @SuppressLint("ConstantLocale")
            val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

            @SuppressLint("ConstantLocale")
            val monthFormatter = SimpleDateFormat("MMM", Locale.getDefault())

            @SuppressLint("ConstantLocale")
            val dayFormatter = SimpleDateFormat("dd", Locale.getDefault())

            @SuppressLint("ConstantLocale")
            val weekDayFormatter = SimpleDateFormat("EEE", Locale.getDefault())
        }
    }

}