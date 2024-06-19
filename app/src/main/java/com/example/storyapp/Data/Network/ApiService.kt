package com.example.storyapp.Data.Network

import com.example.storyapp.Data.AuthData.LoginResp
import com.example.storyapp.Data.Response.AddStoryResp
import com.example.storyapp.Data.Response.DetailStoryResp
import com.example.storyapp.Data.Response.GetAllStoriesResp
import com.example.storyapp.Data.Response.RegisterResp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResp

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResp

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") auth: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): GetAllStoriesResp

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") auth: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = 30,
        @Query("location") location: Int = 1
    ): GetAllStoriesResp

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResp

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): AddStoryResp
}