package com.example.storyapp.Data.StoryData

sealed class StoryResult<out R> private constructor() {

    object isLoading : StoryResult<Nothing>()

    data class isSuccess<out T>(val data: T) : StoryResult<T>()
    data class isError(val error: String) : StoryResult<Nothing>()
}