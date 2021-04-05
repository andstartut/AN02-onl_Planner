package io.techmeskills.an02onl_plannerapp.screen.addNew

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentAddNewBinding
import io.techmeskills.an02onl_plannerapp.screen.main.MainViewModel
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import org.joda.time.DateTime
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddNewFragment : NavigationFragment<FragmentAddNewBinding>(R.layout.fragment_add_new),
    DatePickerListener {

    companion object {
        const val   NOTE_TEXT = "noteText"
        const val DATE = "date"
        const val NEW_NOTE = "newNote"
    }

    private var date: String = ""

    override val viewBinding: FragmentAddNewBinding by viewBinding()

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new, container, false)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        viewBinding.datePicker
            .setListener(this)
            .setOffset(3)
            .setDateSelectedColor(R.color.alfa_corso)
            .setDateSelectedTextColor(Color.WHITE)
            .setMonthAndYearTextColor(Color.DKGRAY)
            .setTodayButtonTextColor(R.color.purple_500)
            .setTodayDateBackgroundColor(Color.GRAY)
            .setUnselectedDayTextColor(Color.DKGRAY)
            .setDayOfWeekTextColor(Color.DKGRAY)
            .showTodayButton(true)
            .init()

        viewBinding.btnCreate.setOnClickListener {
            val noteText = viewBinding.etTypeNote.text.toString()
            if (noteText.isNotBlank()) {
                setFragmentResult(NEW_NOTE, Bundle().apply {
                    putString(NOTE_TEXT, noteText)
                    putString(DATE, date)
                })
            }
            findNavController().popBackStack()
        }
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.datePicker.setVerticalMargin(50, 50)
        viewBinding.btnCreate.setPadding(0, 10, 0, 10)
    }

    override fun onDateSelected(dateSelected: DateTime?) {
        date = dateSelected.toString()
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}
