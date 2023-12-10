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
import retrofit2.Callback

interface ApiRepository {
    fun login(user: LoginRequest, callback: Callback<LoginResponse>)
    fun register(user: UserRequest, callback: Callback<Void>)
    fun forgotPassword(email: ForgotPasswordRequest, callback: Callback<Void>)
    fun getUser(userId: Integer, callback: Callback<UserResponse>)
    fun updateUser(user: UpdateUserRequest, callback: Callback<Void>)
    fun createManga(manga: MangaRequest, callback: Callback<Void>)
    fun updateManga(manga: MangaRequest, callback: Callback<Void>)
    fun deleteManga(mangaId: String, callback: Callback<Void>)
    fun getManga(mangaId: String, callback: Callback<MangaResponse>)
    fun getMangas(search: SearchRequest, callback: Callback<List<MangaListItemResponse>>)
}