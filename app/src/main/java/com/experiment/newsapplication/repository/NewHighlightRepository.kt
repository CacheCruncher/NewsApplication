package com.experiment.newsapplication.repository

import android.util.Log
import com.experiment.newsapplication.api.NewsAPI
import com.experiment.newsapplication.data.NewsHighlight
import javax.inject.Inject

class NewHighlightRepository @Inject constructor(private val newsAPI: NewsAPI) {
    suspend fun getNewsResponse(): List<NewsHighlight> {
        try {
            return newsAPI.getNewsResponse().body()?.articles?.map {
                NewsHighlight(
                    title = it.title,
                    newsUrl = it.url,
                    imageUrl = it.urlToImage,
                    isBookMark = false
                )
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e("NewsAPI", "Error fetching news: ${e.message}", e) // Log exceptions!
            return emptyList() // Important: Handle exceptions gracefully
        }
    }
}