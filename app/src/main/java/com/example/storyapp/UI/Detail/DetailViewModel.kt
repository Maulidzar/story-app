package com.example.storyapp.UI.Detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.Data.Response.Story
import com.example.storyapp.Data.StoryData.StoryRepo
import com.example.storyapp.Data.StoryData.StoryResult
import kotlinx.coroutines.launch

class DetailViewModel(private val storyRepository: StoryRepo) : ViewModel() {

    private val _story = MutableLiveData<StoryResult<Story>>()
    val story : LiveData<StoryResult<Story>>
        get() = _story

    fun getDetailStories(id: String){
        viewModelScope.launch {
            storyRepository.getDetailStories(id).collect{
                _story.value = it
            }
        }
    }
}