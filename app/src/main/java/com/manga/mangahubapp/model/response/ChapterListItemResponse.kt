package com.manga.mangahubapp.model.response

import com.google.gson.annotations.SerializedName

data class ChapterListItemResponse(

    val chapterId: String?,

    @SerializedName("title")
    val title: String?
)
