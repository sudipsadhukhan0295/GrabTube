package com.lifewithtech.grabtube.repository

import android.content.Context
import android.os.Environment
import com.lifewithtech.grabtube.data.NetworkDataSource
import com.lifewithtech.grabtube.model.MediaDetail
import com.lifewithtech.grabtube.model.MediaDetailResponse
import com.lifewithtech.grabtube.network.ApiResponse
import com.lifewithtech.grabtube.network.ApiResult
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
    suspend fun getResponse(url: String): Flow<ApiResult<MediaDetailResponse>> {
        return flow {
            emit(ApiResult.InProgress)
            val result = dataSource.getDownloadUrl(url)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDownloadFile(url: String): Flow<ApiResponse<MediaDetail>> {
        return flow {
            var directory = Environment.DIRECTORY_DOWNLOADS

            val result = dataSource.getDownloadFile(url)
            if (result.responseBody != null) {
                val response = result.responseBody!!
                val directoryPath = appContext.getExternalFilesDir(null)?.absolutePath

                val file = File(directoryPath, "file.mp3")

                val media = MediaDetail()
                try {
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
                                    media.downloading = true
                                    media.progress = ((progressBytes * 100) / totalBytes).toString()
                                    emit(ApiResponse(media))

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

                    media.downloading = false
                    media.downloadPath = file.path
                    emit(ApiResponse(media))
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

    suspend fun getSearchList(value: String): Flow<ApiResponse<ArrayList<MediaDetail>>> {
        return flow {
            val result = dataSource.getSearchResult(value)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}