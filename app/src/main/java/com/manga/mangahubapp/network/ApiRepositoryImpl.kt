package com.manga.mangahubapp.network

import android.util.Log
import com.manga.mangahubapp.model.enums.Genre
import com.manga.mangahubapp.model.request.ForgotPasswordRequest
import com.manga.mangahubapp.model.request.LoginRequest
import com.manga.mangahubapp.model.request.MangaRequest
import com.manga.mangahubapp.model.request.SearchRequest
import com.manga.mangahubapp.model.request.UpdateMangaRequest
import com.manga.mangahubapp.model.request.UpdateUserRequest
import com.manga.mangahubapp.model.request.UserRequest
import com.manga.mangahubapp.model.response.ChapterListItemResponse
import com.manga.mangahubapp.model.response.ChapterResponse
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
        .baseUrl("https://db61-46-98-183-128.ngrok-free.app/")
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

    override fun getUser(token: String, userId: Int, callback: Callback<UserResponse>) {
        service.getUser(token, userId).enqueue(callback)
    }

    override fun updateUser(token: String, user: UpdateUserRequest, callback: Callback<Void>) {
        service.updateUser(token, user).enqueue(callback)
    }

    override fun createManga(token: String, manga: MangaRequest, callback: Callback<Void>) {
        service.createManga(token, manga).enqueue(callback)
    }

    override fun updateManga(token: String, manga: UpdateMangaRequest, callback: Callback<Void>) {
        service.updateManga(token, manga).enqueue(callback)
    }

    override fun deleteManga(token: String, mangaId: String, callback: Callback<Void>) {
        service.deleteManga(token, mangaId).enqueue(callback)
    }

    override fun getManga(token: String, mangaId: String, callback: Callback<MangaResponse>) {
        service.getManga(token, mangaId).enqueue(callback)
    }

    override fun getMangas(
        token: String, search: SearchRequest,
        callback: Callback<List<MangaListItemResponse>>
    ) {
        var genre: Int? = null

        if (search.genre.isNotBlank()) {
            genre = Genre.valueOf(search.genre.toString()).ordinal + 1
        }

        Log.d("Search: ", genre.toString())

        if (search.searchQuery.isNotBlank() && !search.genre.isNullOrEmpty() && search.rating != 0.0) {
            service.getMangas(token, search.searchQuery, genre!!, search.rating)
                .enqueue(callback)
        } else if (search.searchQuery.isNotBlank() && search.genre.isNullOrEmpty() && search.rating != 0.0) {
            service.getMangas(token, search.rating, search.searchQuery)
                .enqueue(callback)
        } else if (search.searchQuery.isNullOrEmpty() && !search.genre.isNullOrEmpty() && search.rating != 0.0) {
            service.getMangas(token, genre!!, search.rating)
                .enqueue(callback)
        } else if (search.searchQuery.isNotBlank() && !search.genre.isNullOrEmpty() && search.rating == 0.0) {
            service.getMangas(token, genre!!, search.searchQuery)
                .enqueue(callback)
        } else if (search.searchQuery.isNotBlank() && search.genre.isNullOrEmpty() && search.rating == 0.0) {
            service.getMangas(token, search.searchQuery)
                .enqueue(callback)
        } else if (search.searchQuery.isNullOrEmpty() && !search.genre.isNullOrEmpty() && search.rating == 0.0) {
            service.getMangas(token, genre!!)
                .enqueue(callback)
        } else if (search.rating != 0.0) {
            service.getMangas(token, search.rating)
                .enqueue(callback)
        } else {
            service.getMangas(token)
                .enqueue(callback)
        }
    }

    override fun getChapters(
        token: String,
        mangaId: String,
        callback: Callback<List<ChapterListItemResponse>>
    ) {
        service.getChapters(token, mangaId).enqueue(callback)
    }


    override fun getChapter(
        token: String,
        chapterId: String,
        callback: Callback<ChapterResponse>
    ) {
        service.getChapter(token, chapterId).enqueue(callback)
    }
}