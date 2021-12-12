package com.lifewithtech.grabtube.network

import android.content.Context
import okio.IOException

class NoConnectivityException(private val context: Context) : IOException() {
    override val message: String
        get() = "No internet connection"
}