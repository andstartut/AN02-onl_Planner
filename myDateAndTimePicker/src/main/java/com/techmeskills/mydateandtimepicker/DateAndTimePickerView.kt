package com.techmeskills.mydateandtimepicker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class DateAndTimePickerView @JvmOverloads constructor(
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
    private val amPmRecyclerView: RecyclerView by lazy {
        findViewById(R.id.rvAmPm)
    }
//    private val btDate: Button by lazy {
//        findViewById(R.id.btnDate)
//    }

    var onDateChangeCallback: DateChangeListener? = null
    var onHourChangeCallback: HourChangeListener? = null
    var onMinuteChangeCallback: MinuteChangeListener? = null
    var onAmPmChangeCallback: AmPmChangeListener? = null

    var days = 365
    private var setCurrentDate: Date? = null

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
        amPmRecyclerView.setPadding(0, padding, 0, padding)

        dateRecyclerView.setHasFixedSize(true)
        hoursRecyclerView.setHasFixedSize(true)
        minutesRecyclerView.setHasFixedSize(true)
        amPmRecyclerView.setHasFixedSize(true)

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
        amPmRecyclerView.layoutManager = SliderLayoutManager(context).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    onAmPmChangeCallback?.onAmPmChanged(amPmList()[layoutPosition])
                }
            }
        }
        dateRecyclerView.adapter = DateAdapter(generateDays(days), setCurrentDate)
        hoursRecyclerView.adapter = TimeAdapter(hoursList(), setCurrentDate)
        minutesRecyclerView.adapter = TimeAdapter(minutesList(), setCurrentDate)
        amPmRecyclerView.adapter = AmPmAdapter(amPmList(), setCurrentDate)
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

    private fun hoursList(): List<Int> {
        val list = arrayListOf<Int>()
        for (i in 1..12) {
            list.add(i)
        }
        return list
    }

    private fun minutesList(): List<Int> {
        val list = arrayListOf<Int>()
        for (i in 0..59) {
            list.add(i)
        }
        return list
    }

    private fun amPmList(): List<Int> {
        val list = arrayListOf<Int>()
        for (i in 0..1) {
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

    interface AmPmChangeListener {
        fun onAmPmChanged(time: Int)
    }

    fun setCurrentDate(date: Date) {
        setCurrentDate = date
    }
}

