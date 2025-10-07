package com.pth.androidapp.presentation.main.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pth.androidapp.domain.models.ImageSlide
import com.pth.androidapp.domain.usecase.GetAllImageSlidesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val throwable: Throwable) : UiState<Nothing>()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllImageSlidesUseCase: GetAllImageSlidesUseCase
) : ViewModel() {

    private val _imageSlides = MutableStateFlow<UiState<List<ImageSlide>>>(UiState.Loading)
    val imageSlides: StateFlow<UiState<List<ImageSlide>>> = _imageSlides.asStateFlow()

    init {
        fetchImageSlides()
    }

    private fun fetchImageSlides() {
        _imageSlides.value = UiState.Loading
        viewModelScope.launch {
            try {
                val slides = getAllImageSlidesUseCase()
                _imageSlides.value = UiState.Success(slides)
            } catch (e: Exception) {
                _imageSlides.value = UiState.Error(e)
            }
        }
    }
}
