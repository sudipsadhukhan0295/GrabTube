package com.lifewithtech.grabtube.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.lifewithtech.grabtube.BaseActivity
import com.lifewithtech.grabtube.R
import com.lifewithtech.grabtube.databinding.ActivityHomeBinding
import com.lifewithtech.grabtube.model.MediaDetail
import com.lifewithtech.grabtube.model.UrlDetail
import com.lifewithtech.grabtube.network.ApiResponse
import com.lifewithtech.grabtube.utils.CustomAudioPlayer
import com.lifewithtech.grabtube.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private var mediaPlayer: CustomAudioPlayer? = null

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

    private val fileDownloadObserver = Observer<ApiResponse<MediaDetail>> { response ->
        if (response != null) {
            if (response.responseBody != null) {
                response.responseBody?.apply {
                    if (downloading) {
                        Toast.makeText(this@HomeActivity, progress, Toast.LENGTH_SHORT).show()
                    } else {
                        setAudioPlayer(downloadPath)
                    }


                }
            }
        }
    }

    private fun setAudioPlayer(downloadPath: String) {
        if (mediaPlayer != null) {
                setDataInMediaPlayer(downloadPath)
            } else {
                mediaPlayer?.release()
                mediaPlayer= CustomAudioPlayer.create()
                setDataInMediaPlayer(downloadPath)
            }
    }

    private fun setDataInMediaPlayer(path: String) {
        mediaPlayer!!.setDataSource(path)
        mediaPlayer!!.prepareAsync()
        mediaPlayer!!.setOnPreparedListener {
            mediaPlayer!!.start()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        mediaPlayer = CustomAudioPlayer.create()

    }

    fun callApi(value: String?) {
        startActivity(Intent(this,SearchActivity::class.java))
        if (value != null)
            viewModel.getResponse(value).observe(this, urlDownloadObserver)
    }


    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
    }
}