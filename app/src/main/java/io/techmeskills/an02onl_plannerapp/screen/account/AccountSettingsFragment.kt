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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        viewModel.currentAccountNameLD.observe(this.viewLifecycleOwner) { accountName ->
            viewBinding.tvName.text = accountName
        }

        viewModel.checkAnyAccountExist.observe(this.viewLifecycleOwner) { isAccountListNotEmpty ->
            if (isAccountListNotEmpty.not()) {
                findNavController().navigateSafe(
                    AccountSettingsFragmentDirections.toNewAccountFragment(
                        tbNewAccount = getString(R.string.create_new),
                        etNewAccount = getString(R.string.create_new_account),
                    )
                )
            }
        }

        viewBinding.btnCreate.setOnClickListener {
            findNavController().navigateSafe(
                AccountSettingsFragmentDirections.toNewAccountFragment(
                    tbNewAccount = getString(R.string.create_new),
                    etNewAccount = getString(R.string.create_new_account),
                )
            )
        }

        viewBinding.btnEdit.setOnClickListener {
            if (viewBinding.etTypeAccountName.isVisible.not()) {
                viewBinding.btnCreate.visibility = View.GONE
                viewBinding.btnDelete.visibility = View.GONE

                viewModel.currentAccountNameLD.observe(this.viewLifecycleOwner) {accountName ->
                    viewBinding.etTypeAccountName.setText(accountName, TextView.BufferType.EDITABLE)
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
                viewBinding.btnEdit.text = getString(R.string.confirm)
            } else {
                if (viewBinding.etTypeAccountName.text.toString().isEmpty().not()) {
                    viewModel.changeAccountName(viewBinding.etTypeAccountName.text.toString())

                    viewBinding.btnEdit.text = getString(R.string.edit)
                    viewBinding.etTypeAccountName.visibility = View.GONE
                    viewBinding.btnCreate.visibility = View.VISIBLE
                    viewBinding.btnDelete.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), R.string.please_enter_your_name, Toast.LENGTH_LONG).show()
                }

            }
        }

        viewBinding.btnDelete.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete)
            .setMessage(R.string.are_your_sure)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                viewModel.deleteAccount()
                dialog.cancel()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
    }

    override val backPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
}