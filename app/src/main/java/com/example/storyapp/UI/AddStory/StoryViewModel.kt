package com.example.storyapp.UI.AddStory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.Data.StoryData.StoryRepo
import java.io.File

class StoryViewModel(private val storyRepo: StoryRepo) : ViewModel() {

    fun uploadStory(description: String, file: File, lat: Double?, lon: Double?) = storyRepo.uploadStory(description, file, lat, lon).asLiveData()
}