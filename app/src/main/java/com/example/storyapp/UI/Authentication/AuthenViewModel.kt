package com.example.storyapp.UI.Authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.Data.AuthData.AuthRepo

class AuthenViewModel(private val authRepository: AuthRepo) : ViewModel() {

    fun register(name: String, email: String, password: String) = authRepository.register(name, email, password).asLiveData()

    fun isLogin() = authRepository.isLogin().asLiveData()

    fun login(email: String, password: String) = authRepository.login(email, password).asLiveData()

}