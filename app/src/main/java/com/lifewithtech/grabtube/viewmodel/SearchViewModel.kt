package com.lifewithtech.grabtube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifewithtech.grabtube.model.MediaDetail
import com.lifewithtech.grabtube.network.ApiResponse
import com.lifewithtech.grabtube.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    ViewModel() {



    fun getSearchResponse(value: String): MutableLiveData<ApiResponse<ArrayList<MediaDetail>>> {
        val result = MutableLiveData<ApiResponse<ArrayList<MediaDetail>>>()

        viewModelScope.launch {
            homeRepository.getSearchList(value).collect {
                result.value = it
            }
        }
        return result
    }

}