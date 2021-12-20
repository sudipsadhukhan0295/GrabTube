package com.lifewithtech.grabtube.utils

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.lifewithtech.grabtube.R

object UiUtil {
    @BindingAdapter("loadUrl")
    @JvmStatic
    fun loadUrl(view: ImageView, imageUrl: String?) {
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .into(view)
    }
}