package com.manga.mangahubapp.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("accessToken")
    val accessToken: String?,

    @SerializedName("refreshToken")
    val refreshToken: String?
)