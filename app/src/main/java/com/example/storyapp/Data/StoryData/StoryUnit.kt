package com.example.storyapp.Data.StoryData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Story")
data class StoryUnit(

    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: String,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "description")
    val desc: String,

    @field:ColumnInfo(name = "photoUrl")
    val photoUrl: String,

    @field:ColumnInfo("createdAt")
    val createdAt: String,

    @field:ColumnInfo(name = "lat")
    val latitude: Double,

    @field:ColumnInfo(name = "lon")
    val longitude: Double
)