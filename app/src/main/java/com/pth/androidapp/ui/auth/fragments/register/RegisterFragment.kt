package com.pth.androidapp.ui.auth.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.pth.androidapp.R
import com.pth.androidapp.base.dialogs.NotifyType
import com.pth.androidapp.base.fragments.BaseFragment
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.common.Utils.setTextChangeListener
import com.pth.androidapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@RegisterFragment.viewModel
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
        tietConfirmPassword.setTextChangeListener(tilConfirmPassword, viewLifecycleOwner)

        navigateToLogin.setOnClickListener {
            navigateToPageAndClearBackStack(
                R.id.action_registerFragment_to_loginFragment,
                R.id.nav_graph
            )
        }
    }

    private fun setupObservers() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            showLoading(result is NetworkResult.Loading)

            when (result) {
                is NetworkResult.Success -> {
                    showNotifyDialog(
                        type = NotifyType.SUCCESS,
                        message = result.data.message,
                        callback = { navigateToPage(R.id.action_registerFragment_to_loginFragment) }
                    )
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