package com.experiment.newsapplication.api

import retrofit2.Response
import retrofit2.http.GET

interface NewsAPI {
    companion object{
        const val BASE_URL = "https://newsapi.org/v2/"
        const val KEY = "NEWS_API_ACCESS_KEY"
    }

    @GET("top-headlines?country=us&apiKey=6adaa4196b1843ae9b2c332005deffae")
    suspend fun getNewsResponse():Response<NewsResponse>
}