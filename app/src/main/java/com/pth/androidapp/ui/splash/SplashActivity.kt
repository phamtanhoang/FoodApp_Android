package com.pth.androidapp.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.net.toUri
import com.pth.androidapp.R
import com.pth.androidapp.base.activities.BaseActivity
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.base.viewmodels.MainViewModel
import com.pth.androidapp.databinding.ActivitySplashBinding
import com.pth.androidapp.ui.auth.AuthActivity
import com.pth.androidapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets(binding)

        playSplashScreenVideo()
        observeLoginState()
    }

    private fun playSplashScreenVideo() {
        val videoUri = "android.resource://${packageName}/${R.raw.splashscreen}".toUri()
        binding.videoView.setVideoURI(videoUri)
        binding.videoView.setOnPreparedListener { it.start() }
        binding.videoView.setOnCompletionListener {
            checkLoginStatus()
            overridePendingTransition(R.anim.slide_up, 0)
        }
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(this) { state ->
            handleLoginState(state)
        }
    }

    private fun checkLoginStatus() {
        viewModel.loginState.value?.let { handleLoginState(it) } ?: navigateToAuth()
    }

    private fun handleLoginState(state: NetworkResult<Boolean>) {
        when (state) {
            is NetworkResult.Success -> {
                showLoading(false)
                handleLoginSuccess(state.data)
            }

            is NetworkResult.Loading -> showLoading(true)
            else -> {
                showLoading(false)
                navigateToAuth()
            }
        }
    }

    private fun handleLoginSuccess(isLoggedIn: Boolean) {
        if (isLoggedIn) navigateToMain() else navigateToAuth()
    }

    private fun navigateToMain() {
        navigateToActivity(MainActivity::class.java)
    }

    private fun navigateToAuth() {
        navigateToActivity(AuthActivity::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.stopPlayback()
    }

}