package com.pth.androidapp.presentation.main

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pth.androidapp.R
import com.pth.androidapp.core.base.activities.BaseActivity
import com.pth.androidapp.databinding.ActivityMainBinding
import com.pth.androidapp.domain.repositories.AuthRepository
import com.pth.androidapp.presentation.auth.AuthActivity
import com.pth.androidapp.presentation.main.navigation.MainTabNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.R)
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var navController: NavController
    private lateinit var tabNavigator: MainTabNavigator

    @Inject
    lateinit var authRepository: AuthRepository

    override fun inflateBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            if (!authRepository.isLoggedIn()) {
                navigateToMainApp()
                finish()
                return@launch
            }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        tabNavigator = MainTabNavigator(this, window, binding, navController)
        tabNavigator.setupTabs()
    }

    fun navigateToMainApp() {
        navigateToActivity(AuthActivity::class.java, finishCurrent = true)
    }

}