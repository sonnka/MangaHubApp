package com.manga.mangahubapp.model.request

import com.manga.mangahubapp.model.enums.Genre
import java.time.LocalDateTime

data class SearchRequest(
    val searchQuery: String,

    val genre: Genre,

    val releasedOn: LocalDateTime,

    val rating: Double
)