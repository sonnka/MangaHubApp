package com.manga.mangahubapp.model.response

import com.google.gson.annotations.SerializedName

data class ChapterResponse(
    @SerializedName("chapterId")
    val chapterId: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("scans")
    val scans: String?,

    @SerializedName("chapterNumber")
    val chapterNumber: Integer,

    @SerializedName("createdOn")
    val createdOn: String?,

    @SerializedName("lastUpdatedOn")
    val lastUpdatedOn: String?,

    @SerializedName("mangaId")
    val mangaId: String?
)