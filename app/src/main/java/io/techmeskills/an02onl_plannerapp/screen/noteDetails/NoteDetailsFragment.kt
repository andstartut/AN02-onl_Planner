package io.techmeskills.an02onl_plannerapp.screen.noteDetails

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.database.model.Note
import io.techmeskills.an02onl_plannerapp.databinding.FragmentNoteDetailsBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class NoteDetailsFragment :
    NavigationFragment<FragmentNoteDetailsBinding>(R.layout.fragment_note_details) {

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    override val viewBinding: FragmentNoteDetailsBinding by viewBinding()

    private val viewModel: NoteDetailsViewModel by viewModel()

    private val args: NoteDetailsFragmentArgs by navArgs()

    @SuppressLint("ResourceAsColor")
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var setEventCondition = false

        args.note?.let { note ->
            viewBinding.run {
                etTypeNote.setText(note.title)
                args.note?.date.let {
                    viewModel.setDate(Date(it!!))
                }
                toolbar.title = getString(R.string.edit_note)
                swEvent.isChecked = args.note?.setEvent!!
            }
        } ?: kotlin.run {
            viewModel.setDate(Date())
        }

        viewBinding.etTypeNote.requestFocus()
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        viewBinding.swEvent.setOnCheckedChangeListener { _, switchCondition ->
            if (switchCondition) {
                setEventCondition = switchCondition
                viewModel.date.observe(this.viewLifecycleOwner) { date ->
                    if (Date().time > date.time) {
                        viewModel.setDate(Date())
                    }
                }
                viewBinding.tvEventDate.setTextColor(ContextCompat.getColor(view.context, R.color.note_yellow_rich))
                viewBinding.tvEventDate.setOnClickListener {
                    dateTimePicker()
                }
            } else {
                setEventCondition = switchCondition
                viewBinding.tvEventDate.setTextColor(ContextCompat.getColor(view.context, R.color.gray))
                viewBinding.tvEventDate.setOnClickListener {
                }
            }
        }

        viewModel.date.observe(this.viewLifecycleOwner) { date ->
            viewBinding.tvEventDate.text = dateFormatter.format(date)

            viewBinding.btnConfirm.setOnClickListener {
                if (viewBinding.etTypeNote.text.isNotBlank()) {
                    args.note?.let {
                        viewModel.editNote(
                            Note(
                                accountName = it.accountName,
                                title = viewBinding.etTypeNote.text.toString(),
                                date = date.time,
                                setEvent = setEventCondition
                            )
                        )
                    } ?: kotlin.run {
                        viewModel.addNote(
                            Note(
                                accountName = "",
                                title = viewBinding.etTypeNote.text.toString(),
                                date = date.time,
                                setEvent = setEventCondition
                            )
                        )
                    }
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.please_enter_your_name), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.btnConfirm.setPadding(0, 10, 0, 10)
    }

    @SuppressLint("ResourceAsColor")
    private fun dateTimePicker() {
        SingleDateAndTimePickerDialog.Builder(context)
            .minutesStep(1)
            .defaultDate(Date())
            .displayHours(true)
            .displayMinutes(true)
            .displayDays(true)
            .mainColor(ContextCompat.getColor(requireContext(), R.color.note_yellow_rich))
            .mustBeOnFuture()
            .bottomSheet()
            .curved()
            .listener {
                viewModel.setDate(it)
            }
            .display()
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}
