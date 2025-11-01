package com.pth.androidapp.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.pth.androidapp.R
import com.pth.androidapp.core.base.activities.BaseActivity
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.databinding.ActivitySplashBinding
import com.pth.androidapp.presentation.auth.AuthActivity
import com.pth.androidapp.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val viewModel: SplashViewModel by viewModels()
    private var isVideoCompleted = false

    override fun inflateBinding(inflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playSplashScreenVideo()
    }

    private fun playSplashScreenVideo() {
        val videoUri = "android.resource://${packageName}/${R.raw.splashscreen}".toUri()
        binding.videoView.apply {
            setVideoURI(videoUri)
            setOnPreparedListener { it.start() }
            setOnCompletionListener {
                isVideoCompleted = true
                proceedToNextScreen()
            }
        }
    }

    private fun proceedToNextScreen() {
        if (isVideoCompleted) {
            lifecycleScope.launch {
                viewModel.isLoggedIn.collectLatest { isLoggedIn ->
                    if (isLoggedIn == null) {
                        return@collectLatest
                    }

                    if (isLoggedIn) {
                        navigateToMain()
                    } else {
                        navigateToAuth()
                    }
                    finish()
                }
            }
        }
    }

    private fun navigateToMain() {
        navigateToActivity(MainActivity::class.java, finishCurrent = true)
    }

    private fun navigateToAuth() {
        overridePendingTransition(R.anim.slide_up, 0)
        navigateToActivity(AuthActivity::class.java, finishCurrent = true)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.stopPlayback()
    }
}