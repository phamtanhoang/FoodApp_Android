package com.pth.androidapp.ui.splash

import android.net.Uri
import android.os.Bundle
import com.pth.androidapp.R
import com.pth.androidapp.base.activities.BaseActivity
import com.pth.androidapp.databinding.ActivitySplashBinding
import com.pth.androidapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()

        setupView()
        playSplashScreenVideo()
    }

    private fun setupView() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets(binding)
    }

    private fun playSplashScreenVideo() {
        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.splashscreen}")
        binding.videoView.setVideoURI(videoUri)

        binding.videoView.setOnPreparedListener { it.start() }
        binding.videoView.setOnCompletionListener {
            overridePendingTransition(R.anim.slide_up, 0)
            navigateToActivity(MainActivity::class.java)
        }
    }

}