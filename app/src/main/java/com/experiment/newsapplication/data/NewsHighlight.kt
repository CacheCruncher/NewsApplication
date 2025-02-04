package com.experiment.newsapplication.data


data class NewsHighlight(
    val title:String?,
    val newsUrl:String,
    val imageUrl:String?,
    val isBookMark:Boolean,
    val createAt: Long = System.currentTimeMillis()
)
