package com.pth.androidapp.presentation.main.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pth.androidapp.core.base.viewmodels.BaseViewModel
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.domain.models.ImageSlide
import com.pth.androidapp.domain.usecases.GetAllImageSlidesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllImageSlidesUseCase: GetAllImageSlidesUseCase
) : BaseViewModel() {

    private val _imageSlides = MutableStateFlow<UiState<List<ImageSlide>>>(UiState.Idle)
    val imageSlides: StateFlow<UiState<List<ImageSlide>>> = _imageSlides.asStateFlow()

    init {
        fetchImageSlides()
    }

    private fun fetchImageSlides() {
        execute(_imageSlides) {
            getAllImageSlidesUseCase()
        }

    }
}
