package com.lifewithtech.grabtube.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import com.lifewithtech.grabtube.BaseActivity
import com.lifewithtech.grabtube.R
import com.lifewithtech.grabtube.databinding.ActivityHomeBinding
import com.lifewithtech.grabtube.model.MediaDetail
import com.lifewithtech.grabtube.model.UrlDetail
import com.lifewithtech.grabtube.network.ApiResponse
import com.lifewithtech.grabtube.network.ApiResult
import com.lifewithtech.grabtube.utils.CustomAudioPlayer
import com.lifewithtech.grabtube.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    @Inject
    lateinit var mediaPlayer: CustomAudioPlayer

    private val viewModel: HomeViewModel by viewModels()

    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_home)
    }

    private val urlDownloadObserver = Observer<ApiResult<UrlDetail>> { response ->
        if (response != null) {
            when (response) {
                is ApiResult.Success -> {

                    viewModel.downloadFile(response.data.url)
                        .observe(this, fileDownloadObserver)
                }
                is ApiResult.Error -> {

                }
                ApiResult.InProgress -> {

                }
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
            mediaPlayer.release()
            //mediaPlayer= CustomAudioPlayer.create()
            setDataInMediaPlayer(downloadPath)
        }
    }

    private fun setDataInMediaPlayer(path: String) {
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //mediaPlayer = CustomAudioPlayer.create()


        initUI()
    }

    private fun initUI() {

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<HomeFragment>(R.id.fcv_home)
        }

        binding.bottomNavHome.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<HomeFragment>(R.id.fcv_home)
                    }
                    true
                }
                R.id.menu_search -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<SearchFragment>(R.id.fcv_home)
                    }

                    true
                }
                else -> false
            }
        }
    }


    override fun onStop() {
        super.onStop()
        mediaPlayer.release()
    }
}