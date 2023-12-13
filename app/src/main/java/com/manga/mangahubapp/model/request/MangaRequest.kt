package com.manga.mangahubapp.model.request

data class MangaRequest(

    val title: String,

    val genre: Int,

    val description: String,

    val releasedOn: String,

    val createdOn: String,

    val coverImage: String,

    val userId: Int
)