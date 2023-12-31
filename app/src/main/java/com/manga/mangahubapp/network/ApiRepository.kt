package com.manga.mangahubapp.network

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
import retrofit2.Callback

interface ApiRepository {
    fun login(
        user: LoginRequest,
        callback: Callback<LoginResponse>
    )

    fun register(
        user: UserRequest,
        callback: Callback<Void>
    )

    fun forgotPassword(
        email: ForgotPasswordRequest,
        callback: Callback<Void>
    )

    fun getUser(
        token: String,
        userId: Int,
        callback: Callback<UserResponse>
    )

    fun updateUser(
        token: String,
        user: UpdateUserRequest,
        callback: Callback<Void>
    )

    fun createManga(
        token: String,
        manga: MangaRequest,
        callback: Callback<Void>
    )

    fun updateManga(
        token: String,
        manga: UpdateMangaRequest,
        callback: Callback<Void>
    )

    fun deleteManga(
        token: String,
        mangaId: String,
        callback: Callback<Void>
    )

    fun getManga(
        token: String,
        mangaId: String,
        callback: Callback<MangaResponse>
    )

    fun getMangas(
        token: String,
        search: SearchRequest,
        callback: Callback<List<MangaListItemResponse>>
    )

    fun getChapters(
        token: String,
        mangaId: String,
        callback: Callback<List<ChapterListItemResponse>>
    )

    fun getChapter(
        token: String,
        chapterId: String,
        callback: Callback<ChapterResponse>
    )
}