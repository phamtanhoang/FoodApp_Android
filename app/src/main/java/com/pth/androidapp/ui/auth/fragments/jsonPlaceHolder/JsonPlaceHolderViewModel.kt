package com.pth.androidapp.ui.auth.fragments.jsonPlaceHolder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.base.viewmodels.BaseViewModel
import com.pth.androidapp.data.models.post.Post
import com.pth.androidapp.data.repositories.JsonPlaceHolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JsonPlaceHolderViewModel @Inject constructor(
    private val jsonPlaceHolderRepository: JsonPlaceHolderRepository
): BaseViewModel() {

    private val _posts = MutableLiveData<NetworkResult<List<Post>>>()
    val posts: LiveData<NetworkResult<List<Post>>> = _posts

    init {
        fetchPageData()
    }

    override fun fetchPageData() {
        fetchAllPosts()
    }

    fun fetchAllPosts() {
        viewModelScope.launch {
            _posts.value = NetworkResult.Loading
            jsonPlaceHolderRepository.getAllPost()
                .catch { e ->
                    _posts.value = NetworkResult.Error(
                        code = 400,
                        message = e.message ?: "Something went wrong!"
                    )
                }
                .collect { result ->
                    _posts.value = result
                }
        }
    }
}