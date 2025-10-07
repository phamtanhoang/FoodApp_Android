package com.pth.androidapp.presentation.main.navigation

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.pth.androidapp.R
import com.pth.androidapp.databinding.ActivityMainBinding

/**
 * Handles tab navigation and UI updates for MainActivity.
 */
@RequiresApi(Build.VERSION_CODES.R)
class MainTabNavigator(
    private val context: Context,
    private val window: Window,
    private val binding: ActivityMainBinding,
    private val navController: NavController
) {
    private val tabs = mutableListOf<Tab>()
    private val fragmentToTab = mutableMapOf<Int, Tab>()

    fun setupTabs() {
        addTab(
            binding.navHomeContainer,
            binding.navHomeIndicator,
            binding.ivNavHome,
            binding.tvNavHome,
            R.id.homeFragment,
            true,
            R.color.white,
            true
        )
        addTab(
            binding.navFoodContainer,
            binding.navFoodIndicator,
            binding.ivNavFood,
            binding.tvNavFood,
            R.id.foodFragment,
            false,
            R.color.white,
            true
        )
        addTab(
            binding.navInstamartContainer,
            binding.navInstamartIndicator,
            binding.ivNavInstamart,
            binding.tvNavInstamart,
            R.id.instamartFragment,
            false,
            R.color.bg_pink,
            true
        )
        addTab(
            binding.navDineoutContainer,
            binding.navDineoutIndicator,
            binding.ivNavDineout,
            binding.tvNavDineout,
            R.id.dineoutFragment,
            false,
            R.color.black,
            false
        )
        addTab(
            binding.navGenieContainer,
            binding.navGenieIndicator,
            binding.ivNavGenie,
            binding.tvNavGenie,
            R.id.genieFragment,
            false,
            R.color.bg_violet,
            true
        )
        setupNavigation()
    }

    private fun addTab(
        container: View,
        indicator: View,
        icon: android.widget.ImageView,
        text: android.widget.TextView,
        fragmentId: Int,
        alwaysOrangeIcon: Boolean,
        @ColorRes statusBarColorRes: Int,
        isLightStatusBar: Boolean
    ) {
        val tab = Tab(
            container,
            indicator,
            icon,
            text,
            fragmentId,
            alwaysOrangeIcon,
            statusBarColorRes,
            isLightStatusBar
        )
        tabs.add(tab)
        fragmentToTab[fragmentId] = tab
    }

    private fun setupNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateUIForDestination(destination.id)
        }
        tabs.forEach { tab ->
            tab.container.setOnClickListener { navController.navigate(tab.fragmentId) }
        }
    }

    private fun updateUIForDestination(destinationId: Int) {
        resetAllViews()
        fragmentToTab[destinationId]?.let { updateTabSelection(it) }
        updateStatusBar(destinationId)
    }

    private fun resetAllViews() {
        tabs.forEach { tab ->
            tab.indicator.visibility = View.GONE
            updateTabStyle(tab, false)
        }
    }

    private fun updateTabSelection(tab: Tab) {
        tab.indicator.visibility = View.VISIBLE
        updateTabStyle(tab, true)
    }

    private fun updateTabStyle(tab: Tab, isSelected: Boolean) {
        val textColor = ContextCompat.getColor(context, if (isSelected) R.color.orange else R.color.dark_grey)
        val iconColor = ContextCompat.getColor(
            context,
            if (isSelected || tab.alwaysOrangeIcon) R.color.orange else R.color.dark_grey
        )
        tab.text.setTextColor(textColor)
        tab.text.typeface = Typeface.create(tab.text.typeface, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        tab.icon.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
    }

    private fun updateStatusBar(destinationId: Int) {
        fragmentToTab[destinationId]?.let { tab ->
            window.statusBarColor = ContextCompat.getColor(context, tab.statusBarColorRes)
            window.decorView.windowInsetsController?.let { controller ->
                val appearance = if (tab.isLightStatusBar) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0
                controller.setSystemBarsAppearance(appearance, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
            }
        }
    }
}
