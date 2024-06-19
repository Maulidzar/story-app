package com.example.storyapp.Data.Injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.storyapp.Data.AuthData.AuthPref
import com.example.storyapp.Data.AuthData.AuthRepo
import com.example.storyapp.Data.Network.ApiConfig
import com.example.storyapp.Data.StoryData.StoryDatabase
import com.example.storyapp.Data.StoryData.StoryRepo

object Injection {

    fun provideAuthRepository(dataStore: DataStore<Preferences>): AuthRepo {
        val apiService = ApiConfig.getApiService()
        val authPreferences = AuthPref.getInstance(dataStore)
        return AuthRepo.getInstance(apiService, authPreferences)
    }

    fun provideStoryRepository(context: Context, dataStore: DataStore<Preferences>): StoryRepo {
        val apiService = ApiConfig.getApiService()
        val authPreferences = AuthPref.getInstance(dataStore)
        val database = StoryDatabase.getInstance(context)
        return StoryRepo.getInstance(apiService, authPreferences, database)
    }
}