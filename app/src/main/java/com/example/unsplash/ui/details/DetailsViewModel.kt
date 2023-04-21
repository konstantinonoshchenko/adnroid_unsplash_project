package com.example.unsplash.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplash.data.Results
import com.example.unsplash.repository.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: UnsplashRepository,
) : ViewModel() {

    private val _photo = MutableLiveData<Results>().apply {
        value=Results()
    }
    val photo:LiveData<Results> = _photo

    fun getPhoto(id:String,token: String){
        viewModelScope.launch {
            _photo.value = repository.getPhotoDetails(id,token)
        }
    }

    fun getDownload(id: String,token: String){
        viewModelScope.launch {
            repository.getDownload(id,token)
        }
    }
    fun liked(id: String, token: String, b: Boolean) {
        viewModelScope.launch {
            repository.liked(id, token, b)
        }
    }
}