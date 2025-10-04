package com.pth.androidapp.presentation.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.pth.androidapp.R
import com.pth.androidapp.core.base.dialogs.NotifyType
import com.pth.androidapp.core.base.fragments.BaseFragment
import com.pth.androidapp.core.common.fold
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
        observeUiState()
    }

    private fun setupNavigation() {
        binding.navigateToRegister.setOnClickListener {
            navigateToPage(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.uiState.collect { state ->
//                state.fold(
//                    onIdle = { showLoading(false) },
//                    onLoading = { showLoading(true) },
//                    onSuccess = {
//                        showLoading(false)
//                        (activity as? AuthActivity)?.navigateToMainApp()
//                    },
//                    onError = { message, _ ->
//                        showLoading(false)
//                        showNotifyDialog(type = NotifyType.ERROR, message = message)
//                    }
//                )
//            }
        }
    }
}