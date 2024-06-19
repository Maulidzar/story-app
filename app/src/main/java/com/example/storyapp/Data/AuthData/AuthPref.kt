package com.example.storyapp.Data.AuthData

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPref private constructor(private val dataStore: DataStore<Preferences>) {

    private val KEY_TOKEN = stringPreferencesKey("token")

    suspend fun logout(){
        dataStore.edit {
            it.clear()
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = "Bearer $token"
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[KEY_TOKEN]
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthPref? = null

        fun getInstance(dataStore: DataStore<Preferences>): AuthPref {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthPref(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}