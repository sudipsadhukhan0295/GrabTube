package com.lifewithtech.grabtube.utils

import android.media.AudioAttributes
import android.media.MediaPlayer

class CustomAudioPlayer : MediaPlayer() {

    private var path: String? = null

    fun getPath(): String? {
        return path
    }


    override fun setDataSource(path: String?) {
        super.setDataSource(path)
        this.path = path
    }

    override fun release() {
        super.release()
        this.path = null
        mediaPlayer = null

    }

    companion object {
        private var mediaPlayer: CustomAudioPlayer? = null

        fun create(): CustomAudioPlayer {
            if (mediaPlayer == null) {
                mediaPlayer = CustomAudioPlayer()
                mediaPlayer?.apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                }

                return mediaPlayer as CustomAudioPlayer
            }
            return mediaPlayer as CustomAudioPlayer
        }
    }
}