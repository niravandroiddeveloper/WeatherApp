package com.wheatherapp.di.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wheatherapp.di.database.dao.BookmarkDao
import com.wheatherapp.di.database.entities.Bookmark

@Database(entities = [Bookmark::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val bookmarkDao: BookmarkDao

}


