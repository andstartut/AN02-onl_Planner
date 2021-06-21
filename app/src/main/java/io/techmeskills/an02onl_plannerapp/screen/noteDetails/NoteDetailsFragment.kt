package io.techmeskills.an02onl_plannerapp.screen.noteDetails

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.transition.MaterialContainerTransform
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = enterTransition(Color.TRANSPARENT, R.id.navHostFragment)
        sharedElementReturnTransition = returnTransition(Color.TRANSPARENT, R.id.navHostFragment)
    }

    @SuppressLint("ResourceAsColor", "ShowToast", "ResourceType")
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        viewBinding.ivToolbarBackground.setImageResource(getImageId())

        var setEventCondition = false
        var count = 0
        var color = ""

        viewBinding.swEvent.setOnCheckedChangeListener { _, switchCondition ->
            if (switchCondition) {
                setEventCondition = switchCondition
                viewModel.date.observe(this.viewLifecycleOwner) { date ->
                    if (date.time < Date().time && count == 0) {
                        count++
                        viewModel.setDate(Date())
                    }
                }

//                val dateTimePickerAnimation = AlphaAnimation(0.1F, 1F)
//                dateTimePickerAnimation.duration = 500
//                dateTimePickerAnimation.startOffset = 300
                viewBinding.dateTimePicker.visibility = View.VISIBLE
//                viewBinding.dateTimePicker.startAnimation(dateTimePickerAnimation)

                viewBinding.tvEventDate.setTextColor(ContextCompat.getColor(view.context, R.color.note_yellow_rich))
            } else {
                setEventCondition = switchCondition
                viewBinding.tvEventDate.setTextColor(ContextCompat.getColor(view.context, R.color.gray))
                viewBinding.dateTimePicker.visibility = View.GONE
            }
        }

        args.note?.let { note ->
            viewBinding.run {
                etTypeNote.setText(note.title)
                viewModel.setDate(Date(note.date))
//                viewBinding.dateTimePicker.setDate(dateFormatter.parse("25.07.2021 13:00")!!)
                topAppBar.title = getString(R.string.edit_note)
                swEvent.isChecked = args.note?.setEvent!!
                setRadioButton(note.color)
                viewModel.setColor(Color.parseColor(note.color))
            }
        } ?: kotlin.run {
            viewModel.setDate(Date())
//            viewBinding.dateTimePicker.setDate(dateFormatter.parse("25.07.2021 13:00")!!)
            color = resources.getString(R.color.main_background)
            viewModel.setColor(Color.parseColor(color))
            setRadioButton(color)
        }

        viewModel.date.observe(this.viewLifecycleOwner) { date ->
            viewBinding.tvEventDate.text = dateFormatter.format(date)
        }

        viewModel.color.observe(this.viewLifecycleOwner) { colorLD ->
            view.setBackgroundColor(colorLD)
            color = "#${Integer.toHexString(colorLD)}"
        }

        viewBinding.radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rbColor1 -> {
                    color = resources.getString(R.color.main_background)
                    viewModel.setColor(Color.parseColor(color))
                }
                R.id.rbColor2 -> {
                    color = resources.getString(R.color.note_background_yellow_lite)
                    viewModel.setColor(Color.parseColor(color))
                }
                R.id.rbColor3 -> {
                    color = resources.getString(R.color.note_background_pink_lite)
                    viewModel.setColor(Color.parseColor(color))
                }
                R.id.rbColor4 -> {
                    color = resources.getString(R.color.note_background_green_lite)
                    viewModel.setColor(Color.parseColor(color))
                }
                R.id.rbColor5 -> {
                    color = resources.getString(R.color.note_background_blue_lite)
                    viewModel.setColor(Color.parseColor(color))
                }
                R.id.rbColor6 -> {
                    color = resources.getString(R.color.note_background_purple_lite)
                    viewModel.setColor(Color.parseColor(color))
                }
            }
        }

        viewBinding.btnConfirm.setOnClickListener {
            if (viewBinding.etTypeNote.text.isNotBlank()) {
                args.note?.let {
                    viewModel.editNote(
                        Note(
                            id = it.id,
                            accountName = it.accountName,
                            title = viewBinding.etTypeNote.text.toString(),
                            date = viewBinding.dateTimePicker.getDate().time,
                            setEvent = setEventCondition,
                            color = color,
                            pinned = it.pinned
                        )
                    )
                } ?: kotlin.run {
                    viewModel.addNote(
                        Note(
                            accountName = "",
                            title = viewBinding.etTypeNote.text.toString(),
                            date = viewBinding.dateTimePicker.getDate().time,
                            setEvent = setEventCondition,
                            color = color
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
        view.postDelayed({
            viewBinding.etTypeNote.requestFocus()
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }, 300)
    }

    @SuppressLint("ResourceType")
    private fun setRadioButton(color: String) {
        when (color) {
            resources.getString(R.color.main_background) -> {
                viewBinding.rbColor1.isChecked = true
            }
            resources.getString(R.color.note_background_yellow_lite) -> {
                viewBinding.rbColor2.isChecked = true
            }
            resources.getString(R.color.note_background_pink_lite) -> {
                viewBinding.rbColor3.isChecked = true
            }
            resources.getString(R.color.note_background_green_lite) -> {
                viewBinding.rbColor4.isChecked = true
            }
            resources.getString(R.color.note_background_blue_lite) -> {
                viewBinding.rbColor5.isChecked = true
            }
            resources.getString(R.color.note_background_purple_lite) -> {
                viewBinding.rbColor6.isChecked = true
            }
        }
        viewModel.setColor(Color.parseColor(color))
    }

    private fun getImageId(): Int {
        val random = Random().nextInt(3)
        return context?.resources!!.getIdentifier(
            "drawable/toolbar_background_$random",
            null,
            context?.packageName
        )
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.btnConfirm.setPadding(0, 10, 0, 10)
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}

fun Fragment.enterTransition(@ColorInt scrim: Int, @IdRes navHostId: Int): MaterialContainerTransform =
    MaterialContainerTransform().apply {
        drawingViewId = navHostId
        duration = resources.getInteger(R.integer.config_screenAnimTime300).toLong()
        interpolator = FastOutSlowInInterpolator()
        scrimColor = scrim
    }

fun Fragment.returnTransition(@ColorInt scrim: Int, @IdRes navHostId: Int): MaterialContainerTransform =
    MaterialContainerTransform().apply {
        drawingViewId = navHostId
        duration = resources.getInteger(R.integer.config_screenAnimTime300).toLong()
        interpolator = FastOutSlowInInterpolator()
        scrimColor = scrim
    }