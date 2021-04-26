package io.techmeskills.an02onl_plannerapp.screen.account

import android.os.Bundle
import android.view.View
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

class NewAccountFragment : NavigationFragment<FragmentNewAccountBinding>(R.layout.fragment_new_account) {

    private val viewModel: NewAccountViewModel by viewModel()

    override val viewBinding: FragmentNewAccountBinding by viewBinding()

    private val args: NewAccountFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.etNewAccount.isNullOrEmpty().not()) {
            viewBinding.toolbar.title = args.tbNewAccount
            viewBinding.tvWelcome.text = args.etNewAccount

           backPressedCallback.isEnabled = true
        }

        viewBinding.btnCreate.setOnClickListener {
            if (viewBinding.etTypeAccountName.text.isNotBlank()) {
                viewModel.createNewAccount(viewBinding.etTypeAccountName.text.toString())
                findNavController().navigateSafe(NewAccountFragmentDirections.toMainFragment())
            } else {
                Toast.makeText(requireContext(), "Please, enter your name", Toast.LENGTH_LONG).show()
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