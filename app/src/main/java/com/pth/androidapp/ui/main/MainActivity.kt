package com.pth.androidapp.ui.main

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pth.androidapp.R
import com.pth.androidapp.base.activities.BaseActivity
import com.pth.androidapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.R)
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val tabs = mutableListOf<Tab>()
    private val fragmentToTab = mutableMapOf<Int, Tab>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets(binding)
        setupTabs()
        setupNavigation()
    }

    private fun setupTabs() {
        val homeTab = Tab(
            container = binding.navHomeContainer,
            indicator = binding.navHomeIndicator,
            icon = binding.ivNavHome,
            text = binding.tvNavHome,
            fragmentId = R.id.homeFragment,
            alwaysOrangeIcon = true,
            statusBarColorRes = R.color.white,
            isLightStatusBar = true
        )
        tabs.add(homeTab)
        fragmentToTab[homeTab.fragmentId] = homeTab

        val foodTab = Tab(
            container = binding.navFoodContainer,
            indicator = binding.navFoodIndicator,
            icon = binding.ivNavFood,
            text = binding.tvNavFood,
            fragmentId = R.id.foodFragment,
            alwaysOrangeIcon = false,
            statusBarColorRes = R.color.white,
            isLightStatusBar = true
        )
        tabs.add(foodTab)
        fragmentToTab[foodTab.fragmentId] = foodTab

        val instamartTab = Tab(
            container = binding.navInstamartContainer,
            indicator = binding.navInstamartIndicator,
            icon = binding.ivNavInstamart,
            text = binding.tvNavInstamart,
            fragmentId = R.id.instamartFragment,
            alwaysOrangeIcon = false,
            statusBarColorRes = R.color.bg_pink,
            isLightStatusBar = true
        )
        tabs.add(instamartTab)
        fragmentToTab[instamartTab.fragmentId] = instamartTab

        val dineoutTab = Tab(
            container = binding.navDineoutContainer,
            indicator = binding.navDineoutIndicator,
            icon = binding.ivNavDineout,
            text = binding.tvNavDineout,
            fragmentId = R.id.dineoutFragment,
            alwaysOrangeIcon = false,
            statusBarColorRes = R.color.black,
            isLightStatusBar = false
        )
        tabs.add(dineoutTab)
        fragmentToTab[dineoutTab.fragmentId] = dineoutTab

        val genieTab = Tab(
            container = binding.navGenieContainer,
            indicator = binding.navGenieIndicator,
            icon = binding.ivNavGenie,
            text = binding.tvNavGenie,
            fragmentId = R.id.genieFragment,
            alwaysOrangeIcon = false,
            statusBarColorRes = R.color.bg_violet,
            isLightStatusBar = true
        )
        tabs.add(genieTab)
        fragmentToTab[genieTab.fragmentId] = genieTab
    }


    private fun resetAllViews() {
        for (tab in tabs) {
            tab.indicator.visibility = View.GONE
            updateTabStyle(tab, false)
        }
    }

    private fun updateUIForDestination(destinationId: Int) {
        val tab = fragmentToTab[destinationId] ?: return
        resetAllViews()
        tab.indicator.visibility = View.VISIBLE
        updateTabStyle(tab, true)
    }

    private fun updateTabStyle(tab: Tab, isSelected: Boolean) {
        val textColorRes = if (isSelected) R.color.orange else R.color.dark_grey
        tab.text.setTextColor(ContextCompat.getColor(this, textColorRes))
        tab.text.typeface =
            Typeface.create(tab.text.typeface, if (isSelected) Typeface.BOLD else Typeface.NORMAL)

        val iconColorRes =
            if (isSelected || tab.alwaysOrangeIcon) R.color.orange else R.color.dark_grey
        tab.icon.setColorFilter(
            ContextCompat.getColor(this, iconColorRes),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateUIForDestination(destination.id)
            updateStatusBar(destination.id)
        }

        for (tab in tabs) {
            tab.container.setOnClickListener {
                navController.navigate(tab.fragmentId)
            }
        }
    }

    private fun updateStatusBar(destinationId: Int) {
        val tab = fragmentToTab[destinationId] ?: return

        window.statusBarColor = ContextCompat.getColor(this, tab.statusBarColorRes)

        val windowInsetsController = window.decorView.windowInsetsController
        if (windowInsetsController != null) {
            if (tab.isLightStatusBar) {
                windowInsetsController.setSystemBarsAppearance(
                    APPEARANCE_LIGHT_STATUS_BARS,
                    APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                windowInsetsController.setSystemBarsAppearance(
                    0, // Reset to default appearance
                    APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        }
    }
}