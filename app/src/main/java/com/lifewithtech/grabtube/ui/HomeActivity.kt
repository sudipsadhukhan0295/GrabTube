package com.lifewithtech.grabtube.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.lifewithtech.grabtube.BaseActivity
import com.lifewithtech.grabtube.R
import com.lifewithtech.grabtube.databinding.ActivityHomeBinding
import com.lifewithtech.grabtube.model.UrlDetail
import com.lifewithtech.grabtube.network.ApiResponse
import com.lifewithtech.grabtube.network.NoConnectionInterceptor
import com.lifewithtech.grabtube.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val viewModel: HomeViewModel by viewModels()

    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_home)
    }

    private val urlDownloadObserver = Observer<ApiResponse<UrlDetail>> { response ->
        if (response != null) {
            if (response.responseBody != null) {
                viewModel.downloadFile(response.responseBody!!.url)
                    .observe(this, fileDownloadObserver)
            }
        }
    }

    private val fileDownloadObserver = Observer<ApiResponse<File>> { response ->
        if (response != null) {
            if (response.responseBody != null) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.fromFile(response.responseBody!!), "audio/mp4")
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    }

    fun callApi(value: String?) {
        if (value != null)
            viewModel.getResponse(value).observe(this, urlDownloadObserver)
    }

    fun openFile(file: File) {

        // Get URI and MIME type of file
        /*val uri: Uri =
            FileProvider.getUriForFile(this, "com.lifewithtech.grabtube"+ ".fileprovider", file)*/
        //val mime = contentResolver.getType(uri)

        // Open file with user selected app
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.type = "video/mp4"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    fun downloadPdf(
        context: Context,
        url: String,
        fileName: String,
        update: (String) -> Unit,
        storedFile: (File) -> Unit,
        exception: (Exception) -> Unit
    ) {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(NoConnectionInterceptor(context))
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder().url(url).build()

        GlobalScope.launch {
            try {
                val response = okHttpClient.newCall(request).execute()

                val body = response.body
                val responseCode = response.code
                if (responseCode >= HttpURLConnection.HTTP_OK &&
                    responseCode < HttpURLConnection.HTTP_MULT_CHOICE &&
                    body != null
                ) {

                    val directoryPath = context.getExternalFilesDir(null)?.absolutePath
                    val file = File("$directoryPath/$fileName.mp4")

                    val length = body.contentLength()
                    var progress = "0"
                    body.byteStream().apply {
                        file.outputStream().use { fileOut ->
                            var bytesCopied = 0
                            val buffer = ByteArray(1024 * 8)
                            var bytes = read(buffer)
                            while (bytes >= 0) {
                                fileOut.write(buffer, 0, bytes)
                                bytesCopied += bytes
                                bytes = read(buffer)
                                progress = ((bytesCopied * 100) / length).toString()
                                update(progress)
                            }

                            storedFile(file)
                        }
                    }
                }
            } catch (e: Exception) {
                exception(e)
            }
        }
    }
}