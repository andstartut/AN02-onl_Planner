package io.techmeskills.an02onl_plannerapp.screen.account

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentAccountSettingsBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import org.koin.android.viewmodel.ext.android.viewModel

class AccountSettingsFragment : NavigationFragment<FragmentAccountSettingsBinding>(R.layout.fragment_account_settings) {

    private val viewModel: AccountSettingsViewModel by viewModel()

    override val viewBinding: FragmentAccountSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentAccountLD.observe(this.viewLifecycleOwner) {
            viewBinding.tvName.text = it.name
        }

        viewBinding.btnCreate.setOnClickListener {
            findNavController().navigateSafe(
                AccountSettingsFragmentDirections.toNewAccountFragment(
                    tbNewAccount = TOOLBAR_NEW_ACCOUNT,
                    etNewAccount = TEXTVIEW_NEW_ACCOUNT,
                )
            )
        }

        viewBinding.btnEdit.setOnClickListener {
            if (viewBinding.etTypeAccountName.isVisible.not()) {
                viewBinding.btnCreate.visibility = View.GONE
                viewBinding.btnDelete.visibility = View.GONE

                viewModel.currentAccountLD.observe(this.viewLifecycleOwner) {
                    viewBinding.etTypeAccountName.setText(it.name, TextView.BufferType.EDITABLE)
                }

                viewBinding.etTypeAccountName.visibility = View.VISIBLE
                viewBinding.etTypeAccountName.requestFocus()
                viewBinding.etTypeAccountName.setSelection(viewBinding.etTypeAccountName.text.toString().length)
                val inputMethodManager =
                    view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
                viewBinding.btnEdit.text = getString(R.string.bt_confirm)
            } else {
                if (viewBinding.etTypeAccountName.text.toString().isNullOrEmpty().not()) {
                    viewModel.changeAccountName(viewBinding.etTypeAccountName.text.toString())

                    viewBinding.btnEdit.text = getString(R.string.bt_edit)
                    viewBinding.etTypeAccountName.visibility = View.GONE
                    viewBinding.btnCreate.visibility = View.VISIBLE
                    viewBinding.btnDelete.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), "Please, enter your name", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

    companion object {
        const val TEXTVIEW_NEW_ACCOUNT = "Create new account:"
        const val TOOLBAR_NEW_ACCOUNT = "New account"

    }
}