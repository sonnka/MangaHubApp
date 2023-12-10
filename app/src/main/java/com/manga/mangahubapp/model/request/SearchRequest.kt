package com.manga.mangahubapp.model.request

import java.time.LocalDateTime

data class SearchRequest(

    val searchQuery: String,

    val genre: String,

    val releasedOn: LocalDateTime?,

    val rating: Double
)