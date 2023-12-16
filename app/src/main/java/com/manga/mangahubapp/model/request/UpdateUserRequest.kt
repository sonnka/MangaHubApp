package com.manga.mangahubapp.model.request

data class UpdateUserRequest(
    val userId: Int,

    var firstName: String,

    var lastName: String,

    val description: String,

    var phoneNumber: String,

    val showConfidentialInformation: Boolean,

    val birthDate: String,

    val email: String,

    val avatar: String
)