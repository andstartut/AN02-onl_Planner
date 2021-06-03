package com.techmeskills.mydatepicker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techmeskills.mydateandtimepicker.R
import java.text.SimpleDateFormat
import java.util.*

class TimeAdapter(
        private var items: List<Int>
) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {
    var selectedHour: Date? = null
        private set


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

        companion object {
            @SuppressLint("ConstantLocale")
            val monthFormatter = SimpleDateFormat("hh", Locale.getDefault())
        }
    }

}