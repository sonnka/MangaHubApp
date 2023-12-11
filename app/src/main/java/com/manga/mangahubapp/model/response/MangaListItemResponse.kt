package com.manga.mangahubapp.model.response

import com.google.gson.annotations.SerializedName

data class MangaListItemResponse(

    @SerializedName("mangaId")
    val mangaId: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("genre")
    val genre: String,

    @SerializedName("coverImage")
    val coverImage: String,

    @SerializedName("rating")
    val rating: Double
)