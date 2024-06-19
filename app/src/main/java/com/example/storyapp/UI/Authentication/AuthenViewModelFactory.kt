package com.example.storyapp.UI.Authentication

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.Data.AuthData.AuthRepo
import com.example.storyapp.Data.Injection.Injection

class AuthViewModelFactory(private val authRepository: AuthRepo) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthenViewModel::class.java)) {
            return AuthenViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: AuthViewModelFactory? = null
        fun getInstance(dataStore: DataStore<Preferences>): AuthViewModelFactory = instance ?: synchronized(this) {
            instance ?: AuthViewModelFactory(Injection.provideAuthRepository(dataStore))
        }.also { instance = it }
    }
}