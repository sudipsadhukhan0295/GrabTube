package com.lifewithtech.grabtube.network

import com.lifewithtech.grabtube.model.MediaDetail
import com.lifewithtech.grabtube.model.MediaDetailResponse
import com.lifewithtech.grabtube.model.UrlDetail
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {

    @POST("https://api.onlinevideoconverter.pro/api/convert")
    suspend fun getResponse(@Body body: HashMap<String,String>): MediaDetailResponse

    @POST("api/search")
    suspend fun getSearchResult(@Body body: RequestBody): ArrayList<MediaDetail>

    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody

    companion object {
        private const val BASE_URL = "http://b102-2409-4060-209b-b6bf-5c2d-e942-c72-196d.ngrok.io/"

        fun create(): ApiService {
            val inspector = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(inspector)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

}