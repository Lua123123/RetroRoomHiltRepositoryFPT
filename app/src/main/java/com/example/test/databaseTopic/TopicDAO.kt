package com.example.test.databaseTopic

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.test.model.Success

@Dao
interface TopicDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopic(success: Success)

    @Query("SELECT * FROM topics")
    fun getListTopic() : LiveData<List<Success>>

    @Query("DELETE FROM topics")
    fun deleteAllRecords()
}