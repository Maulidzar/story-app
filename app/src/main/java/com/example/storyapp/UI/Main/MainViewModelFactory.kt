package com.example.storyapp.UI.Main

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.Data.AuthData.AuthRepo
import com.example.storyapp.Data.Injection.Injection
import com.example.storyapp.Data.StoryData.StoryRepo

class MainViewModelFactory(private val storyRepo: StoryRepo, private val authRepo: AuthRepo) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyRepo, authRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: MainViewModelFactory? = null
        fun getInstance(context: Context, dataStore: DataStore<Preferences>): MainViewModelFactory = instance ?: synchronized(this) {
            instance ?: MainViewModelFactory(Injection.provideStoryRepository(context, dataStore), Injection.provideAuthRepository(dataStore))
        }.also { instance = it }
    }
}