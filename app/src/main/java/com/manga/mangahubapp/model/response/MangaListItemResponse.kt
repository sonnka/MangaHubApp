package com.manga.mangahubapp.model.response

import com.google.gson.annotations.SerializedName

data class MangaListItemResponse(

    @SerializedName("MangaId")
    val mangaId: String,

    @SerializedName("Title")
    val title: String,

    @SerializedName("Genre")
    val genre: String,

    @SerializedName("CoverImage")
    val coverImage: String,

    @SerializedName("Rating")
    val rating: Double
)