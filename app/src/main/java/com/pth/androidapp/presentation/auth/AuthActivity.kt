package com.pth.androidapp.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.pth.androidapp.core.base.activities.BaseActivity
import com.pth.androidapp.core.common.LanguageManager
import com.pth.androidapp.databinding.ActivityAuthBinding
import com.pth.androidapp.domain.repositories.AuthRepository
import com.pth.androidapp.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {
    @Inject
    lateinit var authRepository: AuthRepository

    override fun inflateBinding(inflater: LayoutInflater): ActivityAuthBinding {
        return ActivityAuthBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            if (authRepository.isLoggedIn()) {
                navigateToMainApp()
                finish()
                return@launch
            }
        }
    }

    fun navigateToMainApp() {
        navigateToActivity(MainActivity::class.java, finishCurrent = true)
    }

}