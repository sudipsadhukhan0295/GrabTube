package com.lifewithtech.grabtube.network

import retrofit2.HttpException

class ApiResponse<T> {
    var responseBody: T? = null
    var errorBody: String? = null
    var exception: Exception? = null
    var responseCode: Int? = null

    constructor(responseBody: T) {
        this.responseBody = responseBody
    }

    constructor(exception: HttpException) {
        this.exception = exception
        this.errorBody = exception.message()
        this.responseCode = exception.code()
    }

    constructor(exception: Exception) {
        this.exception = exception
    }
}
