package com.manga.mangahubapp.network

import com.manga.mangahubapp.model.request.ForgotPasswordRequest
import com.manga.mangahubapp.model.request.LoginRequest
import com.manga.mangahubapp.model.request.MangaRequest
import com.manga.mangahubapp.model.request.UpdateMangaRequest
import com.manga.mangahubapp.model.request.UpdateUserRequest
import com.manga.mangahubapp.model.request.UserRequest
import com.manga.mangahubapp.model.response.ChapterListItemResponse
import com.manga.mangahubapp.model.response.ChapterResponse
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
    fun getUser(
        @Header("Authorization") token: String,
        @Query("userId") userId: Int
    ): Call<UserResponse>

    @POST("api/User/update-user-profile")
    @Headers("Content-Type: application/json")
    fun updateUser(
        @Header("Authorization") token: String,
        @Body user: UpdateUserRequest
    ): Call<Void>

    @POST("api/Mangas")
    @Headers("Content-Type: application/json")
    fun createManga(
        @Header("Authorization") token: String,
        @Body manga: MangaRequest
    ): Call<Void>

    @POST("api/Mangas")
    @Headers("Content-Type: application/json")
    fun updateManga(
        @Header("Authorization") token: String,
        @Body manga: UpdateMangaRequest
    ): Call<Void>

    @DELETE("api/Mangas")
    fun deleteManga(
        @Header("Authorization") token: String,
        @Query("mangaId") manga: String
    ): Call<Void>

    @GET("api/Mangas")
    fun getManga(
        @Header("Authorization") token: String,
        @Query("mangaId") manga: String
    ): Call<MangaResponse>

    @GET("api/Mangas/get-all-filter")
    fun getMangas(
        @Header("Authorization") token: String,
        @Query("SearchRequest") search: String,
        @Query("Genre") genre: Int,
        @Query("Rating") rating: Double
    ): Call<List<MangaListItemResponse>>

    @GET("api/Mangas/get-all-filter")
    fun getMangas(
        @Header("Authorization") token: String,
        @Query("Genre") genre: Int,
        @Query("Rating") rating: Double
    ): Call<List<MangaListItemResponse>>

    @GET("api/Mangas/get-all-filter")
    fun getMangas(
        @Header("Authorization") token: String,
        @Query("Genre") genre: Int,
        @Query("SearchQuery") search: String
    ): Call<List<MangaListItemResponse>>


    @GET("api/Mangas/get-all-filter")
    fun getMangas(
        @Header("Authorization") token: String,
        @Query("Rating") rating: Double,
        @Query("SearchQuery") search: String
    ): Call<List<MangaListItemResponse>>

    @GET("api/Mangas/get-all-filter")
    fun getMangas(
        @Header("Authorization") token: String,
        @Query("SearchQuery") search: String
    ): Call<List<MangaListItemResponse>>

    @GET("api/Mangas/get-all-filter")
    fun getMangas(
        @Header("Authorization") token: String,
        @Query("Genre") genre: Int
    ): Call<List<MangaListItemResponse>>

    @GET("api/Mangas/get-all-filter")
    fun getMangas(
        @Header("Authorization") token: String,
        @Query("Rating") rating: Double
    ): Call<List<MangaListItemResponse>>

    @GET("api/Mangas/get-all-filter")
    fun getMangas(
        @Header("Authorization") token: String
    ): Call<List<MangaListItemResponse>>


    @GET("api/Chapters/get-manga-chapters")
    fun getChapters(
        @Header("Authorization") token: String,
        @Query("mangaId") mangaId: String
    ): Call<List<ChapterListItemResponse>>


    @GET("api/Chapters")
    fun getChapter(
        @Header("Authorization") token: String,
        @Query("chapterId") chapterId: String
    ): Call<ChapterResponse>

}