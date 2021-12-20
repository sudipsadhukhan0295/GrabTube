package com.lifewithtech.grabtube.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lifewithtech.grabtube.R
import com.lifewithtech.grabtube.adapter.SearchAdapter
import com.lifewithtech.grabtube.databinding.ActivitySearchBinding
import com.lifewithtech.grabtube.model.MediaDetail
import com.lifewithtech.grabtube.network.ApiResponse
import com.lifewithtech.grabtube.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_search) as ActivitySearchBinding
    }

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchAdapter


    private val searchResultObserver = Observer<ApiResponse<ArrayList<MediaDetail>>> { response ->
        if (response != null) {
            if (response.responseBody != null) {
                adapter.submitList(response.responseBody!!)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SearchAdapter()

        binding.rvSearchList.adapter = adapter


        binding.abtnSearch.setOnClickListener {
            viewModel.getSearchResponse(binding.etSearch.text.toString())
                .observe(this, searchResultObserver)
        }
    }
}