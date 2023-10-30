package com.alkeshapp.audiophoria.data.models


import com.google.gson.annotations.SerializedName

data class GetSongsListResponse(
    @SerializedName("data")
    val `data`: List<SongDto>
)