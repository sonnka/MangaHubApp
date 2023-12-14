package com.manga.mangahubapp.model.request

data class UpdateUserRequest(
    val userId: Int,

    val avatar: String,

    val description: String,

    val showConfidentialInformation: Boolean,

    val birthDate: String,

    val email: String
)