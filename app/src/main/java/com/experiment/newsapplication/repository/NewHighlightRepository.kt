package com.experiment.newsapplication.repository

import com.experiment.newsapplication.api.NewsAPI
import com.experiment.newsapplication.data.NewsHighlight

class NewHighlightRepository(private val newsAPI: NewsAPI) {
    suspend fun getNewsResponse(): List<NewsHighlight> {
        return newsAPI.getNewsResponse().body()?.newsHiglights?.map {
            NewsHighlight(
                title = it.title,
                newsUrl = it.url,
                imageUrl = it.urlToImage,
                isBookMark = false
            )
        } ?: emptyList()
    }
}