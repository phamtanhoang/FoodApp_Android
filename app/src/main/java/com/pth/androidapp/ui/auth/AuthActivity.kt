package com.pth.androidapp.ui.auth

import android.os.Bundle
import com.pth.androidapp.base.activities.BaseActivity
import com.pth.androidapp.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.ui.main.MainActivity


@AndroidEntryPoint
class AuthActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)

        checkLoginStatus()
        setContentView(binding.root)
        setupWindowInsets(binding)
    }

    private fun checkLoginStatus() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is NetworkResult.Success -> {
                    showLoading(false)
                    if (state.data == true) {
                        navigateToActivity(MainActivity::class.java)
                    }
                }
                is NetworkResult.Loading -> {
                    showLoading(true)
                }
                else -> {
                    showLoading(false)
                }
            }
        }
    }
}