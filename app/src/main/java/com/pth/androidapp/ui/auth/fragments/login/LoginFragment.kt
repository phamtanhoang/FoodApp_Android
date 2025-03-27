package com.pth.androidapp.ui.auth.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.pth.androidapp.base.fragments.BaseFragment
import com.pth.androidapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import com.pth.androidapp.R
import com.pth.androidapp.base.dialogs.NotifyType
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.common.Utils.setTextChangeListener

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@LoginFragment.viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
    }

    private fun setupUI() = with(binding) {
        tietEmail.setTextChangeListener(tilEmail, viewLifecycleOwner)
        tietPassword.setTextChangeListener(tilPassword, viewLifecycleOwner)

        navigateToRegister.setOnClickListener {
            navigateToPage(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            showLoading(result is NetworkResult.Loading)

            when (result) {
                is NetworkResult.Success -> {
                    navigateToPage(R.id.action_loginFragment_to_main_nav_graph)
                }
                is NetworkResult.Error -> {
                    showNotifyDialog(NotifyType.ERROR, result.message)
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}