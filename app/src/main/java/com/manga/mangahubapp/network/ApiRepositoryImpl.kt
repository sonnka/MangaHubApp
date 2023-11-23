package com.manga.mangahubapp.network

import com.manga.mangahubapp.model.ForgotPasswordRequest
import com.manga.mangahubapp.model.LoginRequest
import com.manga.mangahubapp.model.LoginResponse
import com.manga.mangahubapp.model.UserRequest
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRepositoryImpl : ApiRepository {

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://56d7-46-98-183-225.ngrok-free.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val service = retrofit.create(ApiService::class.java)

    override fun login(user: LoginRequest, callback: Callback<LoginResponse>) {
        service.login(user).enqueue(callback)
    }

    override fun register(user: UserRequest, callback: Callback<Void>) {
        service.register(user).enqueue(callback)
    }

    override fun forgotPassword(email: ForgotPasswordRequest, callback: Callback<Void>) {
        service.forgotPassword(email).enqueue(callback)
    }


//
//    fun fetchDataFromUrl(url: String, callback: Callback<Any>) {
//        service.fetchData(url).enqueue(callback)
//    }
}