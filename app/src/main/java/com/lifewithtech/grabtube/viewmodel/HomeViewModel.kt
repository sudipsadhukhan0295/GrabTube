package com.lifewithtech.grabtube.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifewithtech.grabtube.model.MediaDetail
import com.lifewithtech.grabtube.model.UrlDetail
import com.lifewithtech.grabtube.network.ApiResponse
import com.lifewithtech.grabtube.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    var searchValue = MutableLiveData<String>()


    fun getResponse(url: String): MutableLiveData<ApiResponse<UrlDetail>> {
        val result = MutableLiveData<ApiResponse<UrlDetail>>()
        viewModelScope.launch {
            homeRepository.getResponse(url).collect {
                result.value = it
            }
        }
        return result
    }

    fun downloadFile(url: String): MutableLiveData<ApiResponse<MediaDetail>> {
        val result = MutableLiveData<ApiResponse<MediaDetail>>()

        viewModelScope.launch {
            homeRepository.getDownloadFile(url).collect {
                result.value = it
            }
        }
        return result
    }

}