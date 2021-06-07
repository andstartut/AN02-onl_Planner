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
    private val items: List<Date>,
    private val setCurrentDate: Date?
) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {
//    private var selectedPosition: Int? = null
    var selectedDay: Date? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.date_picker_item, parent, false),
            setCurrentDate
        )
    }

//    private fun onItemSelect(pos: Int) {
////        val prevPos = items.indexOf(selectedDay)
//        selectedDay = items[pos]
//        onDateChangeCallback(items[pos])
////        notifyItemChanged(prevPos)
//        notifyItemChanged(pos)
//    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(items[position], selectedDay == items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DateViewHolder(
        itemView: View,
        setCurrentDate: Date?,
//            onSelect: (Int) -> Unit,
//            selectedPosition: Int?
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvMonth = itemView.findViewById<TextView>(R.id.tvMonth)
        private val tvDay = itemView.findViewById<TextView>(R.id.tvDay)
        private val tvWeekDay = itemView.findViewById<TextView>(R.id.tvWeekDay)



//        init {
//            itemView.setOnClickListener {
//                onClick(adapterPosition)
//            }
//        }

        @SuppressLint("ResourceAsColor")
        fun bind(date: Date, selected: Boolean) {
            tvMonth.text = monthFormatter.format(date)
            tvDay.text = dayFormatter.format(date)
            tvWeekDay.text = weekDayFormatter.format(date)


//            itemView.setBackgroundColor(
//                    if (selected) R.color.design_default_color_primary else 0
//            )
//
//            val color = if (selected) Color.WHITE else Color.BLACK
//            tvMonth.setTextColor(color)
//            tvWeekDay.setTextColor(color)
//            tvDay.setTextColor(color)
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