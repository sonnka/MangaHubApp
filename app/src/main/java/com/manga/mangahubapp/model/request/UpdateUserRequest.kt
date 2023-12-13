package com.manga.mangahubapp.model.request

data class UpdateUserRequest(
    val userId: Int,

    val firstName: String,

    val lastName: String,

    val avatar: String,

    val description: String,

    val phoneNumber: String,

    val showConfidentialInformation: Boolean,

    val birthDate: String,

    val email: String
)