package com.experiment.newsapplication.data

import androidx.room.Entity


@Entity(tableName = "news_highlight_table")
data class NewsHighlight(
    val title:String?,
    val newsUrl:String,
    val imageUrl:String?,
    val isBookMark:Boolean,
    val createAt: Long = System.currentTimeMillis()
)




