package com.pth.androidapp.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.pth.androidapp.core.base.activities.BaseActivity
import com.pth.androidapp.databinding.ActivityAuthBinding
import com.pth.androidapp.presentation.main.MainActivity
import com.pth.androidapp.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    private val viewModel: MainViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater): ActivityAuthBinding {
        return ActivityAuthBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.isLoggedIn.collectLatest { isLoggedIn ->
                if (isLoggedIn == null) {
                    return@collectLatest
                }

                if (isLoggedIn) {
                    navigateToMain()
                    finish()
                }
            }
        }
    }

    fun navigateToMain() {
        navigateToActivity(MainActivity::class.java, finishCurrent = true)
    }

}