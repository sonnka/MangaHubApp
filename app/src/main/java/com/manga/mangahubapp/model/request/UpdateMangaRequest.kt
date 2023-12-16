package com.manga.mangahubapp.model.request

data class UpdateMangaRequest(
    val mangaId: String,

    val title: String,

    val genre: Int,

    val genres: HashMap<Int, String>,

    val description: String,

    val releasedOn: String,

    val lastUpdatedOn: String,

    val coverImage: String,

    val userId: Int
)
