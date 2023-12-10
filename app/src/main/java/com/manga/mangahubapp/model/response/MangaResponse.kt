package com.manga.mangahubapp.model.response

import com.manga.mangahubapp.model.enums.Genre
import java.time.LocalDateTime

data class MangaResponse(
    val mangaId: String,

    val title: String,

    val genre: Genre,

    val description: String,

    val releasedOn: LocalDateTime,

    val createdOn: LocalDateTime,

    val lastUpdatedOn: LocalDateTime,

    val coverImage: String,

    val userId: Integer,

    val rating: Double
)
