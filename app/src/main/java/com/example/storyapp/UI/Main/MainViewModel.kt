package com.example.storyapp.UI.Main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.Data.AuthData.AuthRepo
import com.example.storyapp.Data.StoryData.StoryRepo
import com.example.storyapp.Data.StoryData.StoryUnit

class MainViewModel(private val storyRepo: StoryRepo, private val authRepo: AuthRepo) : ViewModel() {

    private val _listStory = MutableLiveData<PagingData<StoryUnit>>()
    private val observer  = Observer<PagingData<StoryUnit>>{ _listStory.value = it }
    val listStory : LiveData<PagingData<StoryUnit>>
        get() = _listStory
    init {
        getAllStories()
    }

    fun isLogin() = authRepo.isLogin().asLiveData()

    fun logout() = authRepo.logout().asLiveData()

    fun getAllStories(){
        storyRepo.getAllStories().cachedIn(viewModelScope).observeForever(observer)
    }
    override fun onCleared() {
        storyRepo.getAllStories().removeObserver(observer)
        super.onCleared()
    }
}