package com.experiment.newsapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(newsHighlights: List<NewsHighlight>)

    @Query("SELECT * FROM news_highlight_table")
    fun getNewsHighlights(): Flow<List<NewsHighlight>>


    @Query("SELECT * FROM news_highlight_table WHERE isBookMark=1")
    fun getAllBookMarkedNewsHighlights():Flow<List<NewsHighlight>>

}