package com.example.test.databaseTopic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.test.model.Success

@Database(entities = [Success::class], version = 1, exportSchema = false)
abstract class TopicDatabase : RoomDatabase() {
    companion object {
        private var DB_INSTANCE: TopicDatabase? = null
        private var DB_NAME: String = "topics.db"

        fun getAppDBInstance(context: Context): TopicDatabase {
            if (DB_INSTANCE == null) {
                DB_INSTANCE = Room.databaseBuilder(context.applicationContext, TopicDatabase::class.java, DB_NAME)
                    .allowMainThreadQueries()
                    .build()
            }
            return DB_INSTANCE as TopicDatabase
        }
    }

    abstract fun topicDAO(): TopicDAO
}