package com.example.test.hilt

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import com.example.test.databaseTopic.TopicDAO
import com.example.test.model.Success
import com.example.test.model.Topic
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class RetroRepository @Inject constructor(private val api: API, private val topicDAO: TopicDAO) {
    private var context: Context? = null
    private var postsList: MutableList<Success> = ArrayList()

    fun conText(context: Context, postsList: MutableList<Success> = ArrayList()) {
        this.context = context
        this.postsList = postsList
        Log.d("iii", context.toString())
    }

    fun getAllRecords(): LiveData<List<Success>> {
        return topicDAO.getListTopic()
    }

    fun insertRecord(success: Success) {
        return topicDAO.insertTopic(success)
    }

    //get the data from api
    fun makeApiCall(query: Int) {
        val call: Call<Topic?>? = api.getTopics(query)
        call?.enqueue(object : Callback<Topic?> {
            override fun onResponse(call: Call<Topic?>, response: Response<Topic?>) {
                if (response.isSuccessful) {
//                    topicDAO.deleteAllRecords()
                    response.body()?.success?.forEach {
                        insertRecord(it)
                    }
                }
            }

            override fun onFailure(call: Call<Topic?>, t: Throwable) {
            }
        })
    }

}