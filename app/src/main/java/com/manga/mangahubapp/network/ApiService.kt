package com.manga.mangahubapp.network

import com.manga.mangahubapp.model.request.ForgotPasswordRequest
import com.manga.mangahubapp.model.request.LoginRequest
import com.manga.mangahubapp.model.request.MangaRequest
import com.manga.mangahubapp.model.request.UpdateUserRequest
import com.manga.mangahubapp.model.request.UserRequest
import com.manga.mangahubapp.model.response.LoginResponse
import com.manga.mangahubapp.model.response.MangaListItemResponse
import com.manga.mangahubapp.model.response.MangaResponse
import com.manga.mangahubapp.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

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

    @POST("api/Mangas")
    fun createManga(manga: MangaRequest): Call<Void>

    @POST("api/Mangas")
    fun updateManga(manga: MangaRequest): Call<Void>

    @DELETE("api/Mangas")
    fun deleteManga(mangaId: String): Call<Void>

    @GET("api/Mangas")
    fun getManga(mangaId: String): Call<MangaResponse>

    @GET("api/Mangas/get-all-filter")
    fun getMangas(
        @Header("Authorization") token: String,
        @Query("SearchRequest") search: String,
        @Query("Genre") genre: String,
        @Query("Rating") rating: Double
    ): Call<List<MangaListItemResponse>>

    @GET("api/Mangas/get-all-filter")
    fun getMangas(
        @Header("Authorization") token: String,
        @Query("Genre") genre: String,
        @Query("Rating") rating: Double
    ): Call<List<MangaListItemResponse>>

    @GET("api/Mangas/get-all-filter")
    fun getMangas(
        @Header("Authorization") token: String,
        @Query("Rating") rating: Double,
        @Query("SearchRequest") search: String
    ): Call<List<MangaListItemResponse>>

    @GET("api/Mangas/get-all")
    fun getMangas(
        @Header("Authorization") token: String
    ): Call<List<MangaListItemResponse>>

}