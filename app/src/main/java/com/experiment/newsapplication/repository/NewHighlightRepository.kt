package com.experiment.newsapplication.repository

import android.util.Log
import androidx.room.withTransaction
import com.experiment.newsapplication.api.NewsAPI
import com.experiment.newsapplication.data.APIResult
import com.experiment.newsapplication.data.NewsDatabase
import com.experiment.newsapplication.data.NewsHighlight
import com.experiment.newsapplication.ui.feature.newshighlight.Refresh
import com.experiment.newsapplication.util.networkBoundResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewHighlightRepository @Inject constructor(
    private val newsAPI: NewsAPI,
    private val newsDatabase: NewsDatabase
) {
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

    fun getNewsResult(refresh: Refresh): Flow<APIResult<List<NewsHighlight>>> {
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
                } ?: send(APIResult.Error(error = Throwable(message = "Empty results")))
            } else {
                send(APIResult.Error(error = Throwable(message = response.errorBody().toString())))
            }
        }
    }

    fun getNewsHighlight(refresh: Refresh) = networkBoundResource(
        fetchFromDatabase = {
            newsDatabase.getNewsDao().getNewsHighlights()
        },
        fetchFromNetwork = {
            newsAPI.getNewsResponse().body()?.articles?.map { newsFromServer ->
                val bookMarkedList =
                    newsDatabase.getNewsDao().getAllBookMarkedNewsHighlights().firstOrNull()
                val isBookMarked = bookMarkedList?.any { newsFromServer.url == it.newsUrl } ?: false
                NewsHighlight(
                    title = newsFromServer.title,
                    newsUrl = newsFromServer.url,
                    imageUrl = newsFromServer.urlToImage,
                    isBookMark = isBookMarked
                )
            }
        },
        insertFetchResultInDataBase = { transformedNews ->
            transformedNews?.let {
                newsDatabase.withTransaction {
                    newsDatabase.getNewsDao().insertNews(it)
                }
            }
        },
        shouldFetchFromNetwork = { cachedNews ->
            if (refresh == Refresh.MANUAL) {
                true
            } else {
                val sortedNews = cachedNews.sortedBy { news ->
                    news.createAt
                }
                val oldestTime = sortedNews.firstOrNull()?.createAt
                // refresh if news is older then 1 hr or no news at all.
                val needRefresh =
                    oldestTime == null || oldestTime < System.currentTimeMillis() - TimeUnit.HOURS.toMillis(
                        1
                    )

                needRefresh
            }
        }

    )
}