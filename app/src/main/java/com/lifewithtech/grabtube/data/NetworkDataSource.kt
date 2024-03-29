package com.lifewithtech.grabtube.data

import com.lifewithtech.grabtube.model.MediaDetail
import com.lifewithtech.grabtube.model.UrlDetail
import com.lifewithtech.grabtube.network.ApiResponse
import com.lifewithtech.grabtube.network.ApiResult
import com.lifewithtech.grabtube.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getDownloadUrl(url: String): ApiResult<UrlDetail> {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("url", url)
            .addFormDataPart("format", "mp3")
            .build()
        return try {
            ApiResult.Success(apiService.getResponse(requestBody))
        } catch (e: HttpException) {
            ApiResult.Error(e)
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    suspend fun getSearchResult(value: String): ApiResponse<ArrayList<MediaDetail>> {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("key", value)
            .build()
        return try {
            ApiResponse(apiService.getSearchResult(requestBody))
        } catch (e: HttpException) {
            ApiResponse(e)
        } catch (e: Exception) {
            ApiResponse(e)
        }
    }

    suspend fun getDownloadFile(url: String): ApiResponse<ResponseBody> {
        return try {
            ApiResponse(apiService.downloadFile(url))
        } catch (e: HttpException) {
            ApiResponse(e)
        } catch (e: Exception) {
            ApiResponse(e)
        }
    }
}