package com.manga.mangahubapp.model.response

data class MangaListItemResponse(
    val mangaId: String,
    val title: String,
    val genre: String,
    val coverImage: String,
    val rating: Double
)