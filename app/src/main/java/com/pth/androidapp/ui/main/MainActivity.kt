package com.pth.androidapp.ui.main

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pth.androidapp.R
import com.pth.androidapp.base.activities.BaseActivity
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.base.viewmodels.MainViewModel
import com.pth.androidapp.databinding.ActivityMainBinding
import com.pth.androidapp.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@Suppress("DEPRECATION")
@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.R)
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    private val tabs = mutableListOf<Tab>()
    private val fragmentToTab = mutableMapOf<Int, Tab>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets(binding)

        observeLoginState()
        initializeTabs()
        setupNavigation()
    }

    private fun initializeTabs() {
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
    }

    private fun addTab(
        container: View,
        indicator: View,
        icon: ImageView,
        text: TextView,
        fragmentId: Int,
        alwaysOrangeIcon: Boolean,
        statusBarColorRes: Int,
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
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateUIForDestination(
                destination.id
            )
        }
        tabs.forEach { tab -> tab.container.setOnClickListener { navController.navigate(tab.fragmentId) } }
    }

    private fun updateUIForDestination(destinationId: Int) {
        resetAllViews()
        fragmentToTab[destinationId]?.let { updateTabSelection(it) }
        updateStatusBar(destinationId)
    }

    private fun resetAllViews() {
        tabs.forEach { tab -> tab.indicator.visibility = View.GONE; updateTabStyle(tab, false) }
    }

    private fun updateTabSelection(tab: Tab) {
        tab.indicator.visibility = View.VISIBLE
        updateTabStyle(tab, true)
    }

    private fun updateTabStyle(tab: Tab, isSelected: Boolean) {
        val textColor =
            ContextCompat.getColor(this, if (isSelected) R.color.orange else R.color.dark_grey)
        val iconColor = ContextCompat.getColor(
            this,
            if (isSelected || tab.alwaysOrangeIcon) R.color.orange else R.color.dark_grey
        )

        tab.text.setTextColor(textColor)
        tab.text.typeface =
            Typeface.create(tab.text.typeface, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        tab.icon.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
    }

    private fun updateStatusBar(destinationId: Int) {
        fragmentToTab[destinationId]?.let { tab ->
            window.statusBarColor = ContextCompat.getColor(this, tab.statusBarColorRes)
            window.decorView.windowInsetsController?.let { controller ->
                val appearance = if (tab.isLightStatusBar) APPEARANCE_LIGHT_STATUS_BARS else 0
                controller.setSystemBarsAppearance(appearance, APPEARANCE_LIGHT_STATUS_BARS)
            }
        }
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(this) { state -> handleLoginState(state) }
    }

    private fun handleLoginState(state: NetworkResult<Boolean>) {
        when (state) {
            is NetworkResult.Success -> handleLoginSuccess(state.data)
            is NetworkResult.Loading -> showLoading(true)
            else -> handleLoginFailure()
        }
    }

    private fun handleLoginSuccess(isLoggedIn: Boolean) {
        showLoading(false)
        if (!isLoggedIn) navigateToAuth()
    }

    private fun handleLoginFailure() {
        showLoading(false)
        navigateToAuth()
    }

    private fun navigateToAuth() {
        navigateToActivity(AuthActivity::class.java)
    }
}