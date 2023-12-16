package com.manga.mangahubapp.model.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("avatar")
    val avatar: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("showConfidentialInformation")
    val showConfidentialInformation: Boolean,

    @SerializedName("birthDate")
    val birthDate: String,

    @SerializedName("email")
    val email: String
)