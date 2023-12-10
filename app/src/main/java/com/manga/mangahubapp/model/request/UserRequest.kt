package com.manga.mangahubapp.model.request

data class UserRequest(
    var login: String,
    var password: String,
    var email: String,
    var firstName: String,
    var lastName: String,
    var description: String,
    var phoneNumber: String,
    var birthDate: String,
    var avatar: String,
    var showConfidentialInformation: Boolean
)
