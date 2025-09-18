package com.multibahana.myapp.domain.model

import com.multibahana.myapp.data.model.getme.GetMeResponseDto
import com.multibahana.myapp.data.model.login.LoginResponseDto

data class UserEntity(
    val id: Int,
    val username: String,
    val email: String,
    val fullName: String,
    val gender: String,
    val profileImage: String,
    var accessToken: String? = null,
    var refreshToken: String? = null
)

fun LoginResponseDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        email = email,
        fullName = "$firstName $lastName",
        gender = gender,
        profileImage = image,
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}

fun GetMeResponseDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        email = email,
        fullName = "$firstName $lastName",
        gender = gender,
        profileImage = image,
    )
}
