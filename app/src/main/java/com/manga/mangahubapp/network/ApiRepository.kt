package com.manga.mangahubapp.network

import com.manga.mangahubapp.model.LoginResponse
import retrofit2.Callback

interface ApiRepository {
    fun login(username: String, password: String, callback: Callback<LoginResponse>)
    fun register(
        login: String, password: String, email: String, firstName: String,
        lastName: String, avatar: String, description: String, phoneNumber: String,
        showConfidentialInformation: String, birthDate: String, callback: Callback<Void>
    )
}