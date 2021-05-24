package io.techmeskills.an02onl_plannerapp.support

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.core.view.MenuItemCompat
import androidx.viewbinding.ViewBinding
import io.techmeskills.an02onl_plannerapp.R


abstract class NavigationFragment<T : ViewBinding>(@LayoutRes layoutResId: Int) :
    SupportFragmentInset<T>(layoutResId) {

    open val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            requireActivity().finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        backPressedCallback.remove()
    }
}