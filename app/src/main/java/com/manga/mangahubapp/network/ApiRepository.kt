package com.manga.mangahubapp.network

import com.manga.mangahubapp.model.LoginResponse
import retrofit2.Callback

interface ApiRepository {
    fun login(username: String, password: String, callback: Callback<LoginResponse>);
}