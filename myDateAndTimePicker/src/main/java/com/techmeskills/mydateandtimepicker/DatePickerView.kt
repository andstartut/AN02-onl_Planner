package com.techmeskills.mydatepicker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.techmeskills.mydateandtimepicker.R
import java.util.*

class DatePickerView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    private val dateRecyclerView: RecyclerView by lazy {
        findViewById(R.id.rvDate)
    }
    private val hoursRecyclerView: RecyclerView by lazy {
        findViewById(R.id.rvHours)
    }
    private val minutesRecyclerView: RecyclerView by lazy {
        findViewById(R.id.rvMinutes)
    }
//    private val btDate: Button by lazy {
//        findViewById(R.id.btnDate)
//    }

    var onDateChangeCallback: DateChangeListener? = null
    var onHourChangeCallback: HourChangeListener? = null
    var onMinuteChangeCallback: MinuteChangeListener? = null

    var days = 31

//    val selectedDate: Date?
//        get() {
//            return (dateRecyclerView.adapter as DateAdapter).selectedDay
//        }

    init {
        View.inflate(context, R.layout.date_picker_view, this)

//        val padding: Int = ScreenUtils.getScreenHeight(this.context) / 2 - ScreenUtils.dpToPx(this, 70)
        val padding = 285
        dateRecyclerView.setPadding(0, padding, 0, padding)
        hoursRecyclerView.setPadding(0, padding, 0, padding)
        minutesRecyclerView.setPadding(0, padding, 0, padding)

        dateRecyclerView.setHasFixedSize(true)
        hoursRecyclerView.setHasFixedSize(true)
        minutesRecyclerView.setHasFixedSize(true)

        dateRecyclerView.layoutManager = SliderLayoutManager(context).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    onDateChangeCallback?.onDateChanged(generateDays(days)[layoutPosition])
                }
            }
        }
        hoursRecyclerView.layoutManager = SliderLayoutManager(context).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    onHourChangeCallback?.onHourChanged(hoursList()[layoutPosition])
                }
            }
        }
        minutesRecyclerView.layoutManager = SliderLayoutManager(context).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    onMinuteChangeCallback?.onMinuteChanged(minutesList()[layoutPosition])
                }
            }
        }
        dateRecyclerView.adapter = DateAdapter(generateDays(days))
        hoursRecyclerView.adapter = TimeAdapter(hoursList())
        minutesRecyclerView.adapter = TimeAdapter(minutesList())
    }

    private fun generateDays(days: Int): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val list = arrayListOf<Date>()

        for (i in 0..days) {
            list.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return list
    }

    private fun hoursList(): MutableList<Int> {
        val list = arrayListOf<Int>()
        for (i in 1..12) {
            list.add(i)
        }
        return list
    }

    private fun minutesList(): MutableList<Int> {
        val list = arrayListOf<Int>()
        for (i in 0..59) {
            list.add(i)
        }
        return list
    }

    interface DateChangeListener {
        fun onDateChanged(date: Date)
    }

    interface HourChangeListener {
        fun onHourChanged(time: Int)
    }

    interface MinuteChangeListener {
        fun onMinuteChanged(time: Int)
    }
}

