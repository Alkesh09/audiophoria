package com.alkeshapp.audiophoria.data.models


import com.alkeshapp.audiophoria.domain.models.Song
import com.google.gson.annotations.SerializedName

data class SongDto(
    @SerializedName("accent")
    val accent: String?,
    @SerializedName("artist")
    val artist: String?,
    @SerializedName("cover")
    val cover: String?,
    @SerializedName("date_created")
    val dateCreated: String?,
    @SerializedName("date_updated")
    val dateUpdated: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("sort")
    val sort: Any?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("top_track")
    val topTrack: Boolean?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("user_created")
    val userCreated: String?,
    @SerializedName("user_updated")
    val userUpdated: String?
)

