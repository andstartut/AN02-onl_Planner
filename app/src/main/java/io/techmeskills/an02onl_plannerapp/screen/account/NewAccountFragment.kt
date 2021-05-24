package io.techmeskills.an02onl_plannerapp.screen.account

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentNewAccountBinding
import io.techmeskills.an02onl_plannerapp.screen.noteDetails.NoteDetailsFragmentArgs
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class NewAccountFragment : NavigationFragment<FragmentNewAccountBinding>(R.layout.fragment_new_account) {

    private val viewModel: NewAccountViewModel by viewModel()

    override val viewBinding: FragmentNewAccountBinding by viewBinding()

    private val args: NewAccountFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun getImageId(): Int {
            val random = Random().nextInt(3)
            return context?.resources!!.getIdentifier(
                "drawable/toolbar_background_$random",
                null,
                context?.packageName
            )
        }
        viewBinding.ivToolbarBackground.setImageResource(getImageId())

        if (args.etNewAccount.isNullOrEmpty().not()) {
            viewBinding.topAppBar.title = args.tbNewAccount
            viewBinding.tvWelcome.text = args.etNewAccount

            backPressedCallback.isEnabled = true
        }

        viewBinding.etTypeAccountName.requestFocus()
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        viewBinding.btnCreate.setOnClickListener {
            if (viewBinding.etTypeAccountName.text.isNotBlank()) {
                viewModel.createNewAccount(viewBinding.etTypeAccountName.text.toString())
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                findNavController().navigateSafe(NewAccountFragmentDirections.toMainFragment())
            } else {
                Toast.makeText(requireContext(), R.string.please_enter_your_name, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {

    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}