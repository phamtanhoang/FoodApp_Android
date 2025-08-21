package com.pth.androidapp.ui.main.fragments.home

import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.base.viewmodels.BaseViewModel
import com.pth.androidapp.data.models.imageSlide.ImageSlide
import com.pth.androidapp.data.repositories.ImageSlideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imageSlideRepository: ImageSlideRepository
) : BaseViewModel() {
    private val _imageSlideState = MutableStateFlow<NetworkResult<List<ImageSlide>>>(NetworkResult.Idle)
    val imageSlideState: StateFlow<NetworkResult<List<ImageSlide>>> = _imageSlideState.asStateFlow()

    init {
        getImageSlides()
    }

    fun getImageSlides() = launch {
        imageSlideRepository.getAllImageSlides().collect { result ->
            _imageSlideState.value = result
        }
    }

}