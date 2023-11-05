package com.manga.mangahubapp.network

import com.manga.mangahubapp.model.LoginRequest
import com.manga.mangahubapp.model.LoginResponse
import com.manga.mangahubapp.model.UserRequest
import retrofit2.Callback

interface ApiRepository {
    fun login(user: LoginRequest, callback: Callback<LoginResponse>)
    fun register(user: UserRequest, callback: Callback<Void>)
}