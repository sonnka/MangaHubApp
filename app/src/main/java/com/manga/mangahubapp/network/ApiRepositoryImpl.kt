package com.manga.mangahubapp.network

import com.manga.mangahubapp.model.request.ForgotPasswordRequest
import com.manga.mangahubapp.model.request.LoginRequest
import com.manga.mangahubapp.model.request.MangaRequest
import com.manga.mangahubapp.model.request.SearchRequest
import com.manga.mangahubapp.model.request.UpdateUserRequest
import com.manga.mangahubapp.model.request.UserRequest
import com.manga.mangahubapp.model.response.LoginResponse
import com.manga.mangahubapp.model.response.MangaListItemResponse
import com.manga.mangahubapp.model.response.MangaResponse
import com.manga.mangahubapp.model.response.UserResponse
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRepositoryImpl : ApiRepository {

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://06da-46-98-183-16.ngrok-free.app/")
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

    override fun getUser(userId: Integer, callback: Callback<UserResponse>) {
        service.getUser(userId).enqueue(callback)
    }

    override fun updateUser(user: UpdateUserRequest, callback: Callback<Void>) {
        service.updateUser(user).enqueue(callback)
    }

    override fun createManga(manga: MangaRequest, callback: Callback<Void>) {
        service.createManga(manga).enqueue(callback)
    }

    override fun updateManga(manga: MangaRequest, callback: Callback<Void>) {
        service.updateManga(manga).enqueue(callback)
    }

    override fun deleteManga(mangaId: String, callback: Callback<Void>) {
        service.deleteManga(mangaId).enqueue(callback)
    }

    override fun getManga(mangaId: String, callback: Callback<MangaResponse>) {
        service.getManga(mangaId).enqueue(callback)
    }

    override fun getMangas(
        token: String,
        search: SearchRequest,
        callback: Callback<List<MangaListItemResponse>>
    ) {
        if (search.searchQuery.isNotBlank() && search.genre.isNotBlank()) {
            service.getMangas(token, search.searchQuery, search.genre, search.rating)
                .enqueue(callback)
        } else if (search.searchQuery.isNotBlank() && search.genre.isBlank()) {
            service.getMangas(token, search.rating, search.searchQuery)
                .enqueue(callback)
        } else {
            service.getMangas(token)
                .enqueue(callback)
        }
    }
}