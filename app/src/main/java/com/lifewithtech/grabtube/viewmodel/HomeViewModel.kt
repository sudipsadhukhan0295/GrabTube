package com.lifewithtech.grabtube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifewithtech.grabtube.model.MediaDetail
import com.lifewithtech.grabtube.model.MediaDetailResponse
import com.lifewithtech.grabtube.network.ApiResponse
import com.lifewithtech.grabtube.network.ApiResult
import com.lifewithtech.grabtube.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    var searchValue = MutableLiveData<String>()


    fun getSearchResponse(value: String): MutableLiveData<ApiResponse<ArrayList<MediaDetail>>> {
        val result = MutableLiveData<ApiResponse<ArrayList<MediaDetail>>>()

        viewModelScope.launch {
            homeRepository.getSearchList(value).collect {
                result.value = it
            }
        }
        return result
    }

    fun getResponse(url: String): MutableLiveData<ApiResult<MediaDetailResponse>> {
        val result = MutableLiveData<ApiResult<MediaDetailResponse>>()
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