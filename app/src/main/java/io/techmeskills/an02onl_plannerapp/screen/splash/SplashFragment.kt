package io.techmeskills.an02onl_plannerapp.screen.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentSplashBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import kotlinx.coroutines.flow.first
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : NavigationFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    override val viewBinding: FragmentSplashBinding by viewBinding()

    private val viewModel: SplashViewModel by viewModel()

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.checkAnyAccountExistLiveData.observe(this.viewLifecycleOwner) {
            if (it) {
                viewBinding.root.postDelayed({
                    findNavController().navigateSafe(SplashFragmentDirections.toMainFragment())
                }, 600)
            } else {
                viewBinding.root.postDelayed({
                    findNavController().navigateSafe(SplashFragmentDirections.toNewAccountFragment(
                        null,
                        null
                    ))
                }, 600)
            }
        }
    }
}