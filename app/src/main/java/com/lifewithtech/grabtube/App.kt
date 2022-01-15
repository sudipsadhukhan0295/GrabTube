package com.lifewithtech.grabtube

import android.app.Application
import androidx.room.Room
import com.lifewithtech.grabtube.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()



    }
}