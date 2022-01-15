package com.lifewithtech.grabtube.model

import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

@Entity(tableName = "media")
data class MediaDetail(
    @Ignore
    var downloading: Boolean = false,
    @Ignore
    var downloadPath: String = "",
    @Ignore
    var progress: String = "",
    var title: String = "",
    var thumbnail: String = "",
    var url: String = "",
    var ext: String = "",
    @SerializedName("name")
    var mediaType: String = "",
    var quality: String = ""
)

data class MediaDetailResponse(
    val meta: MetaData,
    val url: ArrayList<MediaDetail>,

    )

data class MetaData(
    var title: String? = "",
    var source: String? = "",
    var duration: String? = "",
)