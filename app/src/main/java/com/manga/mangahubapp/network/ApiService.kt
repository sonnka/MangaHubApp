package com.manga.mangahubapp.network

import com.manga.mangahubapp.model.LoginRequest
import com.manga.mangahubapp.model.LoginResponse
import com.manga.mangahubapp.model.UserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/Auth/login")
    fun login(@Body loginData: LoginRequest): Call<LoginResponse>

    @POST("/api/User/register")
    fun register(@Body registrationData: UserRequest): Call<Void>
//
//    @GET
//    fun fetchData(@Url url: String): Call<Any>
}