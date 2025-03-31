package com.pth.androidapp.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.pth.androidapp.R
import com.pth.androidapp.base.activities.BaseActivity
import com.pth.androidapp.databinding.ActivitySplashBinding
import com.pth.androidapp.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri
import com.pth.androidapp.ui.main.MainActivity

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        playSplashScreenVideo()
    }

    private fun setupView() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets(binding)
    }

    private fun playSplashScreenVideo() {
        val videoUri = "android.resource://${packageName}/${R.raw.splashscreen}".toUri()
        binding.videoView.setVideoURI(videoUri)
        binding.videoView.setOnPreparedListener { it.start() }
        binding.videoView.setOnCompletionListener {
            overridePendingTransition(R.anim.slide_up, 0)
            navigateToActivity(MainActivity::class.java)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.stopPlayback()
    }

}