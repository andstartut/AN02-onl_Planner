package com.techmeskills.mydatepicker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
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
//    private val btToday: Button by lazy {
//        findViewById(R.id.myDatePicker_bt_today)
//    }

    var onDateChangeCallback: DateChangeListener? = null

    val selectedDate: Date?
        get() {
            return (dateRecyclerView.adapter as DateAdapter).selectedDay
        }

    init {
//        LayoutInflater.from(context).inflate(R.layout., this, true)
        context.theme?.let { theme ->
            val attrs =
                    theme.obtainStyledAttributes(attributeSet, R.styleable.DataPickerView, 0, 0)
            val days = attrs.getInteger(R.styleable.DataPickerView_maxDaysCount, 31) - 1

            View.inflate(context, R.layout.date_picker_view, this)
            dateRecyclerView.layoutManager = SliderLayoutManager(context).apply {
                callback = object : SliderLayoutManager.OnItemSelectedListener {
                    override fun onItemSelected(layoutPosition: Int) {
                        onDateChangeCallback?.onDateChanged(generateDays(days)[layoutPosition])
                    }
                }
            }

            hoursRecyclerView.layoutManager = SliderLayoutManager(context)
//            minutesRecyclerView.layoutManager = SliderLayoutManager(context)
            dateRecyclerView.adapter = DateAdapter(generateDays(days))
            hoursRecyclerView.adapter = TimeAdapter(generateHours())
            minutesRecyclerView.adapter = TimeAdapter(generateMinutes())
            attrs.recycle()
        }
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

    private fun generateHours(): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val list = arrayListOf<Date>()

        for (i in 0..12) {
            list.add(calendar.time)
            calendar.add(Calendar.HOUR, 1)
        }
        return list
    }

    private fun generateMinutes(): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val list = arrayListOf<Date>()

        for (i in 0..59) {
            list.add(calendar.time)
            calendar.add(Calendar.MINUTE, 1)
        }
        return list
    }
    fun setDate(date: Date) {
        (dateRecyclerView.adapter as DateAdapter).defaultDate = date
    }


    interface DateChangeListener {
        fun onDateChanged(date: Date)
    }
}