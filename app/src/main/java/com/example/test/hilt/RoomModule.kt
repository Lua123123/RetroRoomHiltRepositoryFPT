package com.example.test.hilt

import android.content.Context
import com.example.test.databaseTopic.TopicDAO
import com.example.test.databaseTopic.TopicDatabase
import com.example.test.model.Topic
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "topics.db"
    val DOMAIN = "http://172.25.48.1/quanlytuvung/public/api/"

    @Provides
    @Singleton
    fun getAppDatabase(@ApplicationContext context: Context): TopicDatabase {
        return TopicDatabase.getAppDBInstance(context)
    }

    @Provides
    @Singleton
    fun getAppDao(topicDatabase: TopicDatabase): TopicDAO {
        return topicDatabase.topicDAO()
    }

    @Provides
    @Singleton
    fun getRetroServiceInstance(retrofit: Retrofit): API {
        return retrofit.create(API::class.java)
    }

    @Provides
    @Singleton
    fun getRetroInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}