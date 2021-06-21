package com.techmeskills.mydateandtimepicker

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Month
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

    private var dateAdapter: DateAdapter
    private var hoursAdapter: TimeAdapter
    private var minutesAdapter: TimeAdapter
    private var amPmAdapter: AmPmAdapter

    private var days = 365

    private var selectedDate: Calendar = Calendar.getInstance().apply { time = Date() }

    init {
        View.inflate(context, R.layout.date_picker_view, this)

        dateRecyclerView.setHasFixedSize(true)
        hoursRecyclerView.setHasFixedSize(true)
        minutesRecyclerView.setHasFixedSize(true)
        amPmRecyclerView.setHasFixedSize(true)

        dateAdapter = DateAdapter(generateDays(days))
        hoursAdapter = TimeAdapter(hoursList())
        minutesAdapter = TimeAdapter(minutesList())
        amPmAdapter = AmPmAdapter(amPmList())

        dateRecyclerView.layoutManager = SliderLayoutManager(context, dateAdapter.itemCount).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    val getDate = generateDays(days)[layoutPosition]
                    val year = yearFormatter.format(getDate).toInt()
                    val month = monthFormatter.format(getDate).toInt() - 1
                    val day = dayFormatter.format(getDate).toInt()
                    selectedDate.set(
                        year, month, day
                    )
                }
            }
        }

        hoursRecyclerView.layoutManager = SliderLayoutManager(context, hoursAdapter.itemCount).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    selectedDate.set(Calendar.HOUR_OF_DAY, hourFormatter.format(hoursList()[layoutPosition]).toInt())
                }
            }
        }
        minutesRecyclerView.layoutManager = SliderLayoutManager(context, minutesAdapter.itemCount).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    selectedDate.set(Calendar.MINUTE, minutesFormatter.format(minutesList()[layoutPosition]).toInt())
                }
            }
        }
        amPmRecyclerView.layoutManager = SliderLayoutManager(context, amPmAdapter.itemCount).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    val amPm = amPmList()[layoutPosition]
                    selectedDate.set(Calendar.AM_PM, if (amPmFormatter.format(amPm).toLowerCase() == "am") 0 else 1)
                }
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val padding: Int = (bottom - top) / 2
        dateRecyclerView.setPadding(0, padding, 0, padding)
        hoursRecyclerView.setPadding(0, padding, 0, padding)
        minutesRecyclerView.setPadding(0, padding, 0, padding)
        amPmRecyclerView.setPadding(0, padding, 0, padding)
        if (dateRecyclerView.adapter == null) {
            dateRecyclerView.adapter = dateAdapter
            hoursRecyclerView.adapter = hoursAdapter
            minutesRecyclerView.adapter = minutesAdapter
            amPmRecyclerView.adapter = amPmAdapter

            dateRecyclerView.snapToFirstPosition()
            hoursRecyclerView.snapToFirstPosition()
            minutesRecyclerView.snapToFirstPosition()
            amPmRecyclerView.snapToFirstPosition()
        }
    }

    private fun RecyclerView.snapToFirstPosition() {
        postDelayed(
            {
                val snapLayoutManager = this.layoutManager as SliderLayoutManager
                val view = snapLayoutManager.findViewByPosition(0)!!
                val snapDistance = snapLayoutManager.snapHelper.calculateDistanceToFinalSnap(
                    snapLayoutManager, view
                )!!
                if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                    scrollBy(snapDistance[0], snapDistance[1])
                }
            }, 50
        )
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

    private fun hoursList(): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val list = arrayListOf<Date>()
        for (i in 0..11) {
            list.add(calendar.time)
            calendar.add(Calendar.HOUR, 1)
        }
        return list
    }

    private fun minutesList(): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val list = arrayListOf<Date>()
        for (i in 0..59) {
            list.add(calendar.time)
            calendar.add(Calendar.MINUTE, 1)
        }
        return list
    }

    private fun amPmList(): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val list = arrayListOf<Date>()
        for (i in 0..1) {
            list.add(calendar.time)
            calendar.add(Calendar.AM_PM, 1)
        }
        return list
    }

    fun getDate(): Date {
        return if (selectedDate.time < Date()) {
            Date()
        } else {
            selectedDate.time
        }
    }

    fun setDate(date: Date) {
        val year = yearFormatter.format(date)
        val month = monthFormatter.format(date)
        val dayInYear = dayInYearFormatter.format(date)
        val hour = hourFormatter.format(date)
        val minutes = minutesFormatter.format(date)
        val amPm = amPmFormatter.format(date)

        dateRecyclerView.scrollToPosition(generateDays(days).indexOf(date))
        hoursRecyclerView.scrollToPosition(hoursList().indexOf(date))
        minutesRecyclerView.scrollToPosition(minutesList().indexOf(date))
        amPmRecyclerView.scrollToPosition(amPmList().indexOf(date))
    }

    @SuppressLint("ConstantLocale")
    val monthFormatter = SimpleDateFormat("MM", Locale.getDefault())

    @SuppressLint("ConstantLocale")
    val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    @SuppressLint("ConstantLocale")
    val dayInYearFormatter = SimpleDateFormat("D", Locale.getDefault())

    @SuppressLint("ConstantLocale")
    val dayFormatter = SimpleDateFormat("dd", Locale.getDefault())

    @SuppressLint("ConstantLocale")
    val hourFormatter = SimpleDateFormat("HH", Locale.getDefault())

    @SuppressLint("ConstantLocale")
    val minutesFormatter = SimpleDateFormat("mm", Locale.getDefault())

    @SuppressLint("ConstantLocale")
    val amPmFormatter = SimpleDateFormat("a", Locale.getDefault())

    interface DateChangeListener {
        fun onDateChanged(day: Int, month: Month, weekDay: DayOfWeek)
    }
}

