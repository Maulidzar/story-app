package com.example.storyapp.UI.Location

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.Data.Injection.Injection
import com.example.storyapp.Data.StoryData.StoryRepo

class MapViewModelFactory(private val storyRepository: StoryRepo) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: MapViewModelFactory? = null
        fun getInstance(context: Context, dataStore: DataStore<Preferences>): MapViewModelFactory = instance ?: synchronized(this) {
            instance ?: MapViewModelFactory(Injection.provideStoryRepository(context, dataStore))
        }.also { instance = it }
    }
}