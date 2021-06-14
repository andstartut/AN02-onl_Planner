package com.techmeskills.mydateandtimepicker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class AmPmAdapter(
    private var items: List<Date>
) : RecyclerView.Adapter<AmPmAdapter.AmPmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmPmViewHolder {
        return AmPmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.time_picker_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AmPmViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class AmPmViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvAmPm = itemView.findViewById<TextView>(R.id.tvTime)

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(date: Date) {
//            if (amPm == 0) {
//                tvAmPm.text = "AM"
//            } else {
//                tvAmPm.text = "PM"
//            }
            tvAmPm.text = amPmFormatter.format(date)
        }

        @SuppressLint("ConstantLocale")
        val amPmFormatter = SimpleDateFormat("a", Locale.getDefault())
    }

}