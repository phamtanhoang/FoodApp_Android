package com.pth.androidapp.presentation.auth.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.pth.androidapp.R
import com.pth.androidapp.core.base.dialogs.NotifyType
import com.pth.androidapp.core.base.fragments.BaseFragment
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.core.utils.ErrorKeys
import com.pth.androidapp.databinding.FragmentLoginBinding
import com.pth.androidapp.presentation.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupNavigation()
        observeLoginState()
    }

    private fun setupNavigation() {
        binding.navigateToRegister.setOnClickListener {
            navigateToPage(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun observeLoginState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is UiState.Loading -> showLoading(true)
                    is UiState.Success -> {
                        showLoading(false)
                        (activity as? AuthActivity)?.navigateToMainApp()
                    }
                    is UiState.Error -> {
                        showLoading(false)
                        // Handle localization directly here
                        val localizedMessage = when (state.message) {
                            ErrorKeys.USER_NOT_FOUND -> getString(R.string.error_user_not_found)
                            ErrorKeys.INVALID_CREDENTIALS -> getString(R.string.error_invalid_credentials)
                            else -> getString(R.string.some_thing_went_wrong_please_try_again_later)
                        }

                        showNotifyDialog(message = localizedMessage, type = NotifyType.ERROR) { }
                    }
                    is UiState.Idle -> showLoading(false)
                }
            }
        }
    }
}