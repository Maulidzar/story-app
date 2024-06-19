package com.example.storyapp.Data.StoryData

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.Data.AuthData.AuthPref
import com.example.storyapp.Data.Network.ApiService
import com.example.storyapp.Data.Response.Story
import com.example.storyapp.Others.EspressoResource
import com.example.storyapp.Others.reduceFileImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRepo private constructor(private val apiService: ApiService, private val authPreferences: AuthPref, private val storyDatabase: StoryDatabase){

    @OptIn(ExperimentalPagingApi::class)
    fun getAllStories(): LiveData<PagingData<StoryUnit>> = liveData {
        EspressoResource.increment()
        authPreferences.getToken().collect{
            if(it != null){
                val response = Pager(
                    config = PagingConfig(pageSize = 5),
                    remoteMediator = StoryRemoteMediator(storyDatabase, apiService, it),
                    pagingSourceFactory = {  storyDatabase.storyDao().getStory() },
                ).liveData
                emitSource(response)
                EspressoResource.decrement()
            }
        }

    }

    fun getDetailStories(id: String): Flow<StoryResult<Story>> = flow {
        emit(StoryResult.isLoading)
        EspressoResource.increment()
        try {
            authPreferences.getToken().collect {
                if (it != null) {
                    val response = apiService.getDetailStories(it, id)
                    emit(StoryResult.isSuccess(response.story))
                    EspressoResource.decrement()
                }
            }
        } catch (e: Exception) {
            emit(StoryResult.isError(e.message.toString()))
            EspressoResource.decrement()
        }
    }

    fun uploadStory(description: String, file: File,latitude: Double?, longitude:Double?): Flow<StoryResult<String>> = flow{
        emit(StoryResult.isLoading)
        EspressoResource.increment()

        val reducedFile = reduceFileImage(file)
        val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
        val desc = description.toRequestBody("text/plain".toMediaType())
        val lat = if (latitude != null) latitude.toString().toRequestBody("text/plain".toMediaType()) else null
        val lon = if (longitude != null) longitude.toString().toRequestBody("text/plain".toMediaType()) else null
        try {
            authPreferences.getToken().collect {
                if (it != null) {
                    val response = apiService.uploadStory(it, imageMultipart, desc, lat, lon)
                    emit(StoryResult.isSuccess(response.message))
                    EspressoResource.decrement()
                }
            }
        } catch (e: Exception) {
            emit(StoryResult.isError(e.message.toString()))
            EspressoResource.decrement()
        }
    }

    fun getStoriesWithLocation(): Flow<StoryResult<List<Story>>> = flow {
        emit(StoryResult.isLoading)
        EspressoResource.increment()
        try {
            authPreferences.getToken().collect {
                if (it != null) {
                    val response = apiService.getStoriesWithLocation(it)
                    emit(StoryResult.isSuccess(response.listStory))
                    EspressoResource.decrement()
                }
            }
        } catch (e: Exception) {
            EspressoResource.decrement()
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepo? = null
        fun getInstance(
            apiService: ApiService,
            authPreferences: AuthPref,
            storyDatabase: StoryDatabase,
        ): StoryRepo =
            instance ?: synchronized(this) {
                instance ?: StoryRepo(apiService, authPreferences, storyDatabase)
            }.also { instance = it }
    }
}