package com.example.test.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Entity(tableName = "topics")
data class Success(

    @field:Expose @field:ColumnInfo(name = "name")
    @field:SerializedName("name")
    var name: String,

    @field:Expose @field:ColumnInfo(name = "soluong")
    @field:SerializedName("soluong")
    var soluong: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id = 0

    @SerializedName("user_create")
    @Expose
    var userCreate: Int? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

}