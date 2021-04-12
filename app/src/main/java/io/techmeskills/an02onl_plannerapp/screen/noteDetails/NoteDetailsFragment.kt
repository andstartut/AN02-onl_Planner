package io.techmeskills.an02onl_plannerapp.screen.noteDetails

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentNoteDetailsBinding
import io.techmeskills.an02onl_plannerapp.screen.main.MainViewModel
import io.techmeskills.an02onl_plannerapp.screen.main.Note
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.joda.time.DateTime
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class NoteDetailsFragment :
    NavigationFragment<FragmentNoteDetailsBinding>(R.layout.fragment_note_details),
    DatePickerListener {

    private var getDate: String = ""

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override val viewBinding: FragmentNoteDetailsBinding by viewBinding()

    private val viewModel: MainViewModel by viewModel()

    private val args: NoteDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dataPickerInit()

        val stringToDate = DateTime(dateFormatter.parse(args.note?.date)?.time)

        viewBinding.btnConfirm.setOnClickListener {
            if (viewBinding.etTypeNote.text.isNotBlank()) {
                setFragmentResult(NOTE_RESULT, Bundle().apply {
                    putParcelable(
                        NOTE,
                        Note(
                            if (args.note == null) NEW_NOTE_INDEX else args.note!!.id,
                            viewBinding.etTypeNote.text.toString(),
                            getDate
                        )
                    )
                })
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please, enter your note", Toast.LENGTH_LONG).show()
            }
        }

        args.note?.let { note ->
            viewBinding.run {
                etTypeNote.setText(note.title)
                if (!args.note?.date.isNullOrBlank()) {
                    datePicker.setDate(stringToDate)
                }
                toolbar.title = TOOLBAR_TITLE
            }
        }
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.btnConfirm.setPadding(0, 10, 0, 10)
    }

    override fun onDateSelected(dateSelected: DateTime?) {
        getDate = dateFormatter.format(dateSelected?.toDate())
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
            .setOffset(60)
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
        const val NEW_NOTE_INDEX = -1
        const val NOTE = "Note"
        const val TOOLBAR_TITLE = "Edit Note"
        const val NOTE_RESULT = "NoteResult"
    }
}
