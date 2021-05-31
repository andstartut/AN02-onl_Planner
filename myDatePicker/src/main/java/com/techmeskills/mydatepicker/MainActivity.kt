package com.techmeskills.mydatepicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dayFormatter = SimpleDateFormat("MMM dd EEE", Locale.getDefault())

        val dateTimePickerView = findViewById<DatePickerView>(R.id.date_picker_view)
        val dateRecyclerView = findViewById<RecyclerView>(R.id.rvDate)
        val hoursRecyclerView = findViewById<RecyclerView>(R.id.rvHours)

        val padding: Int = ScreenUtils.getScreenHeight(this) / 2 - ScreenUtils.dpToPx(this, 70)
        dateRecyclerView.setPadding(0, padding, 0, padding)
        hoursRecyclerView.setPadding(0, padding, 0, padding)



        dateTimePickerView.onDateChangeCallback = object : DatePickerView.DateChangeListener {
            override fun onDateChanged(date: Date) {
                Toast.makeText(applicationContext, dayFormatter.format(date), Toast.LENGTH_LONG).show()
            }
        }
    }
}