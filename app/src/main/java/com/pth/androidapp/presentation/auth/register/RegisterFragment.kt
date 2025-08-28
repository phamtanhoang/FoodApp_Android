package com.pth.androidapp.presentation.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pth.androidapp.core.base.dialogs.NotifyType
import com.pth.androidapp.core.base.fragments.BaseFragment
import com.pth.androidapp.core.common.fold
import com.pth.androidapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
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

        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state.fold(
                    onIdle = { showLoading(false) },
                    onLoading = { showLoading(true) },
                    onSuccess = {
                        showLoading(false)
                        showNotifyDialog(type = NotifyType.SUCCESS, message = "Đăng ký thành công!") {
                            findNavController().popBackStack()
                        }
                    },
                    onError = { message, _ ->
                        showLoading(false)
                        showNotifyDialog(message = message, type = NotifyType.ERROR)
                    }
                )
            }
        }
    }
}