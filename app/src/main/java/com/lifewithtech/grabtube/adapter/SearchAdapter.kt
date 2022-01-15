package com.lifewithtech.grabtube.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lifewithtech.grabtube.R
import com.lifewithtech.grabtube.databinding.ViewSearchItemBinding
import com.lifewithtech.grabtube.model.MediaDetail

class SearchAdapter(private var onDownloadClick: (MediaDetail) -> Unit) :
    ListAdapter<MediaDetail, SearchAdapter.ViewHolder>(MediaDiffCallback()) {


    class ViewHolder(val binding: ViewSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.view_search_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.adapter = this
        holder.binding.mediaDetail = getItem(position)

    }

    fun downloadFile(mediaDetail: MediaDetail){
        onDownloadClick(mediaDetail)
    }
}

private class MediaDiffCallback : DiffUtil.ItemCallback<MediaDetail>() {
    override fun areItemsTheSame(
        oldItem: MediaDetail,
        newItem: MediaDetail
    ): Boolean {
        return oldItem.title == newItem.title

    }

    override fun areContentsTheSame(
        oldItem: MediaDetail,
        newItem: MediaDetail
    ): Boolean {
        return oldItem == newItem
    }

}