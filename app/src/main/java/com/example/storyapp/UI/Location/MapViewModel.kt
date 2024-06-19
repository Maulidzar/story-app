package com.example.storyapp.UI.Location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.Data.Response.Story
import com.example.storyapp.Data.StoryData.StoryRepo
import com.example.storyapp.Data.StoryData.StoryResult
import kotlinx.coroutines.launch

class MapViewModel(private val storyRepository: StoryRepo) : ViewModel() {

    private val _story = MutableLiveData<StoryResult<List<Story>>>()
    val story : LiveData<StoryResult<List<Story>>>
        get() = _story

    init{
        getStoriesWithLocation()
    }

    private fun getStoriesWithLocation(){
        viewModelScope.launch {
            storyRepository.getStoriesWithLocation().collect{
                _story.value = it
            }
        }
    }

}