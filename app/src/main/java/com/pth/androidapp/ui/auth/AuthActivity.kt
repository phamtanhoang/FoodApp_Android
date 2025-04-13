package com.pth.androidapp.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import com.pth.androidapp.base.activities.BaseActivity
import com.pth.androidapp.databinding.ActivityAuthBinding
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.base.viewmodels.MainViewModel
import com.pth.androidapp.common.LanguageManager
import com.pth.androidapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var languageManager: LanguageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets(binding)

        setupLanguageSpinner()
        observeLoginState()
    }

    private fun setupLanguageSpinner() {
        languageManager = LanguageManager(this)
        binding.spinnerLanguage.apply {
            adapter = LanguageAdapter(this@AuthActivity, languageManager.getAvailableLanguages())
            setSelection(languageManager.getCurrentLanguagePosition())
            onItemSelectedListener = createLanguageSelectionListener()
        }
    }

    private fun createLanguageSelectionListener(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                handleLanguageSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun handleLanguageSelection(position: Int) {
        val selectedLanguage = languageManager.getAvailableLanguages()[position]
        if (selectedLanguage.code != languageManager.getCurrentLanguageCode()) {
            languageManager.setLanguage(selectedLanguage.code)
            restart()
        }
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(this) { state ->
            handleLoginState(state)
        }
    }

    private fun handleLoginState(state: NetworkResult<Boolean>) {
        when (state) {
            is NetworkResult.Success -> {
                showLoading(false)
                handleLoginSuccess(state.data)
            }

            is NetworkResult.Loading -> showLoading(true)
            else -> showLoading(false)
        }
    }

    private fun handleLoginSuccess(isLoggedIn: Boolean) {
        if (isLoggedIn) navigateToMain()
    }

    private fun navigateToMain() {
        navigateToActivity(MainActivity::class.java)
    }

}