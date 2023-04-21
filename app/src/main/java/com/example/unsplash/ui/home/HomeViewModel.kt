package com.example.unsplash.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.unsplash.data.Token
import com.example.unsplash.repository.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {

    private val currentQuery = MutableLiveData<Authorization>()

    val photos = currentQuery.switchMap {
        repository.getSearchResults(it.query, it.token).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String, token: String) {
        currentQuery.value = Authorization(query, token)
    }

    fun getToken(code: String?): Call<Token> {
        return repository.getToken(code)}

    fun liked(id: String, token: String, b: Boolean) {
        viewModelScope.launch {
            repository.liked(id, token, b)
        }
    }
}

data class Authorization(
    val query: String,
    val token: String,

)