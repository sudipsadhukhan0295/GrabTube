package com.lifewithtech.grabtube.network

import com.lifewithtech.grabtube.model.UrlDetail
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {

    @POST("api/download")
    suspend fun getResponse(@Body body: RequestBody): UrlDetail

    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody

    companion object {
        private const val BASE_URL = "http://dccc-223-191-16-178.ngrok.io/"

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