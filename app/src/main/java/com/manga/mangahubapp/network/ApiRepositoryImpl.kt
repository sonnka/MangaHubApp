package com.manga.mangahubapp.network

import com.manga.mangahubapp.model.LoginResponse
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRepositoryImpl : ApiRepository {

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://4b60-46-98-183-225.ngrok-free.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val service = retrofit.create(ApiService::class.java)

    override fun login(username: String, password: String, callback: Callback<LoginResponse>) {
        val loginData = mapOf("login" to username, "password" to password)
        service.login(loginData).enqueue(callback)
    }

//    fun register(username: String, email: String, password: String, callback: Callback<User>) {
//        val registrationData =
//            mapOf("username" to username, "email" to email, "password" to password)
//        service.register(registrationData).enqueue(callback)
//    }
//
//    fun fetchDataFromUrl(url: String, callback: Callback<Any>) {
//        service.fetchData(url).enqueue(callback)
//    }
}