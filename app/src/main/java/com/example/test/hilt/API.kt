package com.example.test.hilt

import com.example.test.model.Topic
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface API {
    @FormUrlEncoded
    @POST("topic/getAllTopic")
    fun getTopics(@Field("user_create") user_create: Int): Call<Topic?>?
}
