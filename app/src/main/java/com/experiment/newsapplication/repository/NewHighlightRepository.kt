package com.experiment.newsapplication.repository

import android.util.Log
import com.experiment.newsapplication.api.NewsAPI
import com.experiment.newsapplication.data.APIResult
import com.experiment.newsapplication.data.NewsHighlight
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
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

    fun getNewsResult(): Flow<APIResult<List<NewsHighlight>>> {
        return channelFlow {
            val response = newsAPI.getNewsResponse()

            Log.d("repository", "getNewsResult: code: ${response.code()} ")
            send(APIResult.Loading())

            // added 2s delay to show progress icon
            delay(2000)

            if (response.isSuccessful) {
                response.body()?.articles?.map {
                    NewsHighlight(
                        title = it.title,
                        newsUrl = it.url,
                        imageUrl = it.urlToImage,
                        isBookMark = false
                    )
                }?.let {
                    send(APIResult.Success(it))
                }?:send(APIResult.Error(error = Throwable(message = "Empty results")))
            } else {
                send(APIResult.Error(error = Throwable(message = response.errorBody().toString())))
            }
        }
    }
}