package com.experiment.newsapplication.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface NewsAPI {
    companion object{
        const val BASE_URL = "https://newsapi.org/v2/"
        const val API_KEY = "6adaa4196b1843ae9b2c332005deffae"
    }

    @GET("top-headlines?country=us&apiKey=6adaa4196b1843ae9b2c332005deffae")
    suspend fun getNewsResponse():Response<NewsResponse>

    @Headers("X-Api-Key: $API_KEY")
    //@GET(value = "top-headlines?country=us&pageSize=100")
    @GET("everything?q=tea&page=1&pageSize=40")
    suspend fun getNewsResponse1():Response<NewsResponse>
}