package com.manga.mangahubapp.model.response

import com.google.gson.annotations.SerializedName

data class MangaResponse(

    @SerializedName("mangaId")
    val mangaId: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("genre")
    val genre: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("releasedOn")
    val releasedOn: String,

    @SerializedName("createdOn")
    val createdOn: String,

    @SerializedName("lastUpdatedOn")
    val lastUpdatedOn: String,

    @SerializedName("coverImage")
    val coverImage: String,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("rating")
    val rating: Double
)
