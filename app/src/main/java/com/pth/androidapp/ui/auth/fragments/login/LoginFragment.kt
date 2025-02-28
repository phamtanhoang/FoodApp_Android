package com.pth.androidapp.ui.auth.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.pth.androidapp.base.fragments.BaseFragment
import com.pth.androidapp.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
//        tokenViewModel.token.observe(viewLifecycleOwner) { token ->
//            if (token != null) {
//                navController.navigate(R.id.action_loginFragment_to_registerFragment)
//            }
//        }
//
//        viewModel.loginResponse.observe(viewLifecycleOwner) { response ->
//            when (response) {
//                is ApiResponse.Failure -> binding.loginTV.text = "Error! ${response.errorMessage}"
//                ApiResponse.Loading -> binding.loginTV.text = "Loading"
//                is ApiResponse.Success -> {
//                    tokenViewModel.saveToken(response.data.token)
//                }
//            }
//        }
    }

    private fun setupListeners() {
//        binding.loginButton.setOnClickListener {
//            val auth = Auth("test@gmail.com", "123Test")
//            viewModel.login(auth, object : CoroutinesErrorHandler {
//                override fun onError(message: String) {
//                    binding.loginTV.text = "Error! $message"
//                }
//            })
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}