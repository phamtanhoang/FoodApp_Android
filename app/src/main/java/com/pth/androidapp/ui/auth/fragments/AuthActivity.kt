package com.pth.androidapp.ui.auth.fragments

import android.os.Bundle
import com.pth.androidapp.base.activities.BaseActivity
import com.pth.androidapp.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
    }

    private fun setupView() {
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets(binding)
    }
}