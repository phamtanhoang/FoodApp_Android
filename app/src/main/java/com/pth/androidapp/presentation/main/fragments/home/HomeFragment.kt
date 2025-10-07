package com.pth.androidapp.presentation.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.pth.androidapp.core.base.fragments.BaseFragment
import com.pth.androidapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeImageSlides()
    }

    private fun observeImageSlides() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.imageSlides.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        // TODO: Show loading UI
                    }
                    is UiState.Success -> {
                        val slides = state.data
                        // TODO: Bind slides to your UI (e.g., adapter)
                    }
                    is UiState.Error -> {
                        // TODO: Show error UI, e.g. state.throwable.message
                    }
                }
            }
        }
    }
}