package com.lifewithtech.grabtube.model

data class MediaDetail(
    var downloading: Boolean = false,
    var downloadPath: String = "",
    var progress: String = "",
    var title: String = "",
    var thumbnail: String = "",
    var url: String = ""
)
