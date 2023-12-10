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
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/Auth/login")
    fun login(@Body loginData: LoginRequest): Call<LoginResponse>

    @POST("/api/User/register")
    fun register(@Body registrationData: UserRequest): Call<Void>

    @POST("/api/User/forgot-password")
    fun forgotPassword(@Body email: ForgotPasswordRequest): Call<Void>

    @GET("api/User")
    fun getUser(userId: Integer): Call<UserResponse>

    @POST("api/User/update-user-profile")
    fun updateUser(user: UpdateUserRequest): Call<Void>
    fun createManga(manga: MangaRequest): Call<Void>
    fun updateManga(manga: MangaRequest): Call<Void>
    fun deleteManga(mangaId: String): Call<Void>
    fun getManga(mangaId: String): Call<MangaResponse>
    fun getMangas(search: SearchRequest): Call<List<MangaListItemResponse>>

}