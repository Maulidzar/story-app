package com.example.storyapp.Data.AuthData

import com.example.storyapp.Data.Network.ApiService
import com.example.storyapp.Data.StoryData.StoryResult
import com.example.storyapp.Others.EspressoResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class AuthRepo private constructor(private val apiService: ApiService, private val authPreferences: AuthPref){

    fun isLogin(): Flow<String?> = flow  {emitAll(authPreferences.getToken())}

    fun register(name: String, email: String, password: String): Flow<StoryResult<String>> = flow {
        emit(StoryResult.isLoading)
        EspressoResource.increment()
        try {
            val respon = apiService.register(name, email, password)
            emit(StoryResult.isSuccess(respon.message))
            EspressoResource.decrement()
        }catch (e: Exception){
            emit(StoryResult.isError(e.message.toString()))
            EspressoResource.decrement()
        }
    }

    fun login(email: String, password: String): Flow<StoryResult<String>> = flow {
        emit(StoryResult.isLoading)
        EspressoResource.increment()
        try {
            val respon = apiService.login(email, password)
            val token = respon.loginResult.token
            authPreferences.saveToken(token)
            emit(StoryResult.isSuccess(respon.message))
            EspressoResource.decrement()
        }catch (e: Exception){
            emit(StoryResult.isError(e.message.toString()))
            EspressoResource.decrement()
        }
    }

    fun logout(): Flow<StoryResult<String>> = flow {
        emit(StoryResult.isLoading)
        authPreferences.logout()
        emit(StoryResult.isSuccess("success"))
    }

    companion object {
        @Volatile
        private var instance: AuthRepo? = null
        fun getInstance(
            apiService: ApiService,
            authPref: AuthPref
        ): AuthRepo =
            instance ?: synchronized(this) {
                instance ?: AuthRepo(apiService, authPref)
            }.also { instance = it }
    }
}