package com.manga.mangahubapp.model.request

data class MangaRequest(

    val title: String,

    val genre: Int,

    val genres: HashMap<Int, String>,

    val description: String,

    val releasedOn: String,

    val createdOn: String,

    val coverImage: String,

    val userId: Int
)