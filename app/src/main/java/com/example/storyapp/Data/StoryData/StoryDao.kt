package com.example.storyapp.Data.StoryData

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryUnit>)

    @Query("SELECT * FROM story")
    fun getStory(): PagingSource<Int, StoryUnit>

    @Query("SELECT * FROM Story")
    fun getAllStories(): List<StoryUnit>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}