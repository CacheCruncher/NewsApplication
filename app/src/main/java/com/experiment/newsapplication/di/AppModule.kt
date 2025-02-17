package com.experiment.newsapplication.di

import android.app.Application
import androidx.room.Room
import com.experiment.newsapplication.api.NewsAPI
import com.experiment.newsapplication.data.NewsDatabase
import com.experiment.newsapplication.network.NewsAPIInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient()
        val interceptor = NewsAPIInterceptor()
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val clientBuilder = client.newBuilder()
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)

        return Retrofit.Builder()
            .baseUrl(NewsAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()
    }


    @Provides
    @Singleton
    fun provideNewsAPI(retrofit: Retrofit): NewsAPI {
        return retrofit.create(NewsAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(application: Application): NewsDatabase {
        return Room.databaseBuilder(application, NewsDatabase::class.java, "news database")
            .fallbackToDestructiveMigration()
            .build()
    }

}