package com.pth.androidapp.presentation.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pth.androidapp.core.base.fragments.BaseFragment
import com.pth.androidapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import com.pth.androidapp.R
import com.pth.androidapp.core.base.dialogs.NotifyType
import com.pth.androidapp.core.common.UiState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private val viewModel: RegisterViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupNavigation()
        observeRegisterState()

    }

    private fun setupNavigation() {
        binding.navigateToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun observeRegisterState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registerState.collect { state ->
                when (state) {
                    is UiState.Loading -> showLoading(true)
                    is UiState.Success -> {
                        showLoading(false)
                        showNotifyDialog(
                            type = NotifyType.SUCCESS,
                            message = getString(R.string.registration_success)
                        ) {
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }
                    }
                    is UiState.Error -> {
                        showLoading(false)
                        showNotifyDialog(message = state.message, type = NotifyType.ERROR) {  }
                    }
                    is UiState.Idle -> showLoading(false)
                }
            }
        }
    }

}