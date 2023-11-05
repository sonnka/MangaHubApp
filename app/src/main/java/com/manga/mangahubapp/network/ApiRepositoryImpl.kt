package com.manga.mangahubapp.network

import com.manga.mangahubapp.model.LoginResponse
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

    override fun login(username: String, password: String, callback: Callback<LoginResponse>) {
        val loginData = mapOf("login" to username, "password" to password)
        service.login(loginData).enqueue(callback)
    }

    override fun register(
        login: String, password: String, email: String, firstName: String,
        lastName: String, avatar: String, description: String, phoneNumber: String,
        showConfidentialInformation: String, birthDate: String, callback: Callback<Void>
    ) {
        val registrationData =
            mapOf(
                "login" to login,
                "password" to password,
                "email" to email,
                "firstName" to firstName,
                "lastName" to lastName,
                "avatar" to avatar,
                "description" to description,
                "phoneNumber" to phoneNumber,
                "showConfidentialInformation" to showConfidentialInformation,
                "birthDate" to birthDate
            )
        service.register(registrationData).enqueue(callback)
    }


//
//    fun fetchDataFromUrl(url: String, callback: Callback<Any>) {
//        service.fetchData(url).enqueue(callback)
//    }
}