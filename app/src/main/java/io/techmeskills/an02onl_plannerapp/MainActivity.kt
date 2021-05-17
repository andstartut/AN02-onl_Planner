package io.techmeskills.an02onl_plannerapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import io.techmeskills.an02onl_plannerapp.databinding.ActivityMainBinding
import io.techmeskills.an02onl_plannerapp.support.SupportActivityInset
import io.techmeskills.an02onl_plannerapp.support.setWindowTransparency


class MainActivity : SupportActivityInset<ActivityMainBinding>() {

    override lateinit var viewBinding: ActivityMainBinding

    private val navHostFragment by lazy { (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment) }
    private val navController: NavController by lazy { navHostFragment.navController }

    override fun onStart() {
        super.onStart()

    }

    @SuppressLint("BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setWindowTransparency(this)
    }

    override fun getActiveFragment(): Fragment? {
        return navHostFragment.childFragmentManager.fragments[0]
    }

}