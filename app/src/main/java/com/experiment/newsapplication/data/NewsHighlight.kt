package com.experiment.newsapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "news_highlight_table")
data class NewsHighlight(
    val title:String?,
    @PrimaryKey val newsUrl:String,
    val imageUrl:String?,
    val isBookMark:Boolean,
    val createAt: Long = System.currentTimeMillis()
)




