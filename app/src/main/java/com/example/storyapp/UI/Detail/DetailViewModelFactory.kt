package com.example.storyapp.UI.Detail

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.Data.Injection.Injection
import com.example.storyapp.Data.StoryData.StoryRepo

class DetailViewModelFactory(private val storyRepo: StoryRepo) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(storyRepo) as T
        }
        throw IllegalArgumentException("Unknown class : " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailViewModelFactory? = null
        fun getInstance(context: Context, dataStore: DataStore<Preferences>): DetailViewModelFactory = instance ?: synchronized(this) {
            instance ?: DetailViewModelFactory(Injection.provideStoryRepository(context, dataStore))
        }.also { instance = it }
    }
}