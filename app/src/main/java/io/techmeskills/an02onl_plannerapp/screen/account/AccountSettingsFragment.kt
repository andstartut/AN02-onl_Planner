package io.techmeskills.an02onl_plannerapp.screen.account

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
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

        viewModel.currentAccountNameLiveData.observe(this.viewLifecycleOwner) {
            viewBinding.tvName.text = it
        }

        viewBinding.btnCreate.setOnClickListener {
            findNavController().navigateSafe(AccountSettingsFragmentDirections.toNewAccountFragment("New account"))
        }

        viewBinding.btnEdit.setOnClickListener {
            if (viewBinding.etTypeAccountName.isVisible.not()) {
                viewBinding.btnCreate.isEnabled = false

                viewModel.currentAccountNameLiveData.observe(this.viewLifecycleOwner) {
                    viewBinding.etTypeAccountName.setText(it, TextView.BufferType.EDITABLE)
                }

                viewBinding.etTypeAccountName.visibility = View.VISIBLE
                viewBinding.etTypeAccountName.requestFocus()
                viewBinding.etTypeAccountName.setSelection(viewBinding.etTypeAccountName.text.toString().length)
                val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
                viewBinding.btnEdit.text = getString(R.string.bt_confirm)
            } else {

                viewModel.changeAccountName(viewBinding.etTypeAccountName.text.toString())

                viewBinding.btnEdit.text = getString(R.string.bt_edit)
                viewBinding.etTypeAccountName.visibility = View.GONE
                viewBinding.btnCreate.isEnabled = true
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
}