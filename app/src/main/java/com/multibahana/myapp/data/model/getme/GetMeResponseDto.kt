package com.multibahana.myapp.data.model.getme

import com.google.gson.annotations.SerializedName

data class GetMeResponseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("image") val image: String,
)

