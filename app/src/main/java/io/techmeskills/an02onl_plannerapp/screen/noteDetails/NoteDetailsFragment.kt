package io.techmeskills.an02onl_plannerapp.screen.noteDetails

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.TokenWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentNoteDetailsBinding
import io.techmeskills.an02onl_plannerapp.screen.main.MainFragment
import io.techmeskills.an02onl_plannerapp.screen.main.MainViewModel
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.joda.time.DateTime
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteDetailsFragment :
    NavigationFragment<FragmentNoteDetailsBinding>(R.layout.fragment_note_details),
    DatePickerListener {

    private var date: String = ""

    override val viewBinding: FragmentNoteDetailsBinding by viewBinding()

    private val viewModel: MainViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dataPickerInit()

        viewBinding.toolbar.title = arguments?.getString(TOOLBAR)
        viewBinding.etTypeNote.setText(arguments?.getString(NOTE_TEXT))
//        viewBinding.datePicker.setDate()
        if (arguments?.getString(TOOLBAR) == MainFragment.TOOLBAR_EDIT) {
            viewBinding.btnConfirm.setOnClickListener {
                val noteText = viewBinding.etTypeNote.text.toString()
                    setFragmentResult(EDIT_NOTE, Bundle().apply {
                        putString(NOTE_TEXT, noteText)
                        putString(DATE, date)
                    })
                findNavController().popBackStack()
            }
        }else{
            viewBinding.btnConfirm.setOnClickListener {
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
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.btnConfirm.setPadding(0, 10, 0, 10)
    }

    override fun onDateSelected(dateSelected: DateTime?) {
        date = dateSelected?.toLocalDate().toString()
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

    @SuppressLint("ResourceAsColor")
    private fun dataPickerInit() {
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
    }

    companion object {
        const val NOTE_TEXT = "noteText"
        const val DATE = "date"
        const val NEW_NOTE = "newNote"
        const val TOOLBAR = "toolbar"
        const val EDIT_NOTE = "editNote"
    }
}
