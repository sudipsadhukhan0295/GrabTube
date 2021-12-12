package com.lifewithtech.grabtube.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import com.lifewithtech.grabtube.data.NetworkDataSource
import com.lifewithtech.grabtube.model.UrlDetail
import com.lifewithtech.grabtube.network.ApiResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val dataSource: NetworkDataSource,
    @ApplicationContext private val appContext: Context
) {
    suspend fun getResponse(url: String): Flow<ApiResponse<UrlDetail>> {
        return flow {
            val result = dataSource.getDownloadUrl(url)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDownloadFile(url: String): Flow<ApiResponse<File>> {
        return flow {
            var directory = Environment.DIRECTORY_DOWNLOADS

            val result = dataSource.getDownloadFile(url)
            if (result.responseBody != null) {
                val response = result.responseBody!!
                val directoryPath = appContext.getExternalFilesDir(null)?.absolutePath

                val file = File(directoryPath, "file.mp3")

                try {
                    Log.e("MEDIA TYPE", response.contentType()?.subtype!!)
                    Log.e("MEDIA TYPE", response.contentType().toString())
                    response.apply {
                        byteStream().use { inputStream ->
                            file.outputStream().use { outputStream ->
                                val totalBytes = contentLength()
                                val data = ByteArray(8_192)
                                var progressBytes = 0L

                                while (true) {
                                    val bytes = inputStream.read(data)

                                    if (bytes == -1) {
                                        break
                                    }

                                    outputStream.channel
                                    outputStream.write(data, 0, bytes)
                                    progressBytes += bytes

                                    //emit(Download.Progress(percent = ((progressBytes * 100) / totalBytes).toInt()))
                                }

                                when {
                                    progressBytes < totalBytes ->
                                        throw Exception("missing bytes")
                                    progressBytes > totalBytes ->
                                        throw Exception("too many bytes")
                                    //else ->
                                    //deleteFile = false
                                }
                            }
                        }
                    }
                    emit(ApiResponse(file))
                    //emit(Download.Finished(file))
                } finally {
                    // check if download was successful

                    /*if (deleteFile) {
                        file.delete()
                    }*/
                }


            }


            //emit(result)
        }.flowOn(Dispatchers.IO)
            .distinctUntilChanged()
    }


}