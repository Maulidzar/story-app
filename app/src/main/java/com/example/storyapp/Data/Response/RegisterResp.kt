package com.example.storyapp.Data.Response

import com.google.gson.annotations.SerializedName

data class RegisterResp(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
