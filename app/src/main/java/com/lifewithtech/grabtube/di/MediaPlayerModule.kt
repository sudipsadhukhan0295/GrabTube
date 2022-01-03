package com.lifewithtech.grabtube.di

import com.lifewithtech.grabtube.utils.CustomAudioPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MediaPlayerModule {
    @Singleton
    @Provides
    fun provideCustomAudioPlayer(): CustomAudioPlayer {
        return CustomAudioPlayer.create()
    }
}