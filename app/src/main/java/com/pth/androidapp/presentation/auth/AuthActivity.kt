package com.pth.androidapp.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import com.google.firebase.auth.FirebaseAuth
import com.pth.androidapp.core.base.activities.BaseActivity
import com.pth.androidapp.core.common.LanguageManager
import com.pth.androidapp.databinding.ActivityAuthBinding
import com.pth.androidapp.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {
    @Inject
    lateinit var languageManager: LanguageManager

    override fun inflateBinding(inflater: LayoutInflater): ActivityAuthBinding {
        return ActivityAuthBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser != null) {
            navigateToMainApp()
            finish()
            return
        }

        setupLanguageSpinner()
    }


    fun navigateToMainApp() {
        navigateToActivity(MainActivity::class.java, finishCurrent = true)
    }

    private fun setupLanguageSpinner() {
        // ...
    }
}