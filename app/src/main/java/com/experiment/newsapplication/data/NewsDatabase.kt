package com.experiment.newsapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsHighlight::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
}