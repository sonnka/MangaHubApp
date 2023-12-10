package com.manga.mangahubapp.model.request

import com.manga.mangahubapp.model.enums.Genre
import java.time.LocalDateTime

data class MangaRequest(

    val title: String,

    val genre: Genre,

    val description: String,

    val releasedOn: LocalDateTime,

    val createdOn: LocalDateTime,

    val coverImage: String,

    val userId: Integer
)