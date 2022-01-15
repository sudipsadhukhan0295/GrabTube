package com.lifewithtech.grabtube.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.lifewithtech.grabtube.R
import com.lifewithtech.grabtube.adapter.SearchAdapter
import com.lifewithtech.grabtube.databinding.FragmentSearchBinding
import com.lifewithtech.grabtube.model.MediaDetail
import com.lifewithtech.grabtube.model.MediaDetailResponse
import com.lifewithtech.grabtube.network.ApiResponse
import com.lifewithtech.grabtube.network.ApiResult
import com.lifewithtech.grabtube.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchAdapter


    private val searchResultObserver = Observer<ApiResponse<ArrayList<MediaDetail>>> { response ->
        if (response != null) {
            if (response.responseBody != null) {
                adapter.submitList(response.responseBody!!)
            }
        }
    }

    private val mediaDownloadUrlObserver = Observer<ApiResult<MediaDetailResponse>> { response ->
        when (response) {
            is ApiResult.Success -> {
                viewModel.downloadFile(response.data.url[2].url).observe(this, downloadObserver)
            }
            is ApiResult.Error -> {

            }
            ApiResult.InProgress -> {

            }
        }
    }

    private val downloadObserver = Observer<ApiResponse<MediaDetail>> { response ->
        if (!response.responseBody!!.downloading){
            val directoryPath = requireActivity().getExternalFilesDir(null)

            val list = directoryPath?.listFiles()
            list?.forEach {
                Log.e("NAME", it.nameWithoutExtension)
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        initUI()
    }

    private fun initUI() {
        binding.fragment = this
        binding.viewmodel = viewModel

        adapter = SearchAdapter { mediaDetail ->
            viewModel.getResponse(mediaDetail.url).observe(this, mediaDownloadUrlObserver)
        }

        binding.rvSearchList.adapter = adapter

        binding.etSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                searchByValue()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    fun searchByValue() {
        viewModel.getSearchResponse(viewModel.searchValue.value ?: "")
            .observe(this, searchResultObserver)
    }
}