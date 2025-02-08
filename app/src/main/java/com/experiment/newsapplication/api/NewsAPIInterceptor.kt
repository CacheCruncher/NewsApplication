package com.experiment.newsapplication.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class NewsAPIInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
       with(chain){
           val method = request().method
           Log.d("NewsAPI", "intercept: method $method")
           val names = request().headers.names()
           for (name in names){
               Log.d("NewsAPI", "intercept header name:  $name")
           }

           val body = request().body.toString()
           Log.d("NewsAPI", "intercept: body $body")
       }
      return  chain.proceed(request = chain.request())
    }
}