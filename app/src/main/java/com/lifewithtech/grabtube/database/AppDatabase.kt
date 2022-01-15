package com.lifewithtech.grabtube.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lifewithtech.grabtube.model.MediaDetail

@Database(entities = [MediaDetail::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbDao(): DbDao

}