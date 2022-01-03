package com.lifewithtech.grabtube.network

import java.lang.Exception

sealed class ApiResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error(val exception: Exception) : ApiResult<Nothing>()
    object InProgress : ApiResult<Nothing>()
}