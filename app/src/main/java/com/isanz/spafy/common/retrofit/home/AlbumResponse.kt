package com.isanz.spafy.common.retrofit.home

import com.google.gson.annotations.SerializedName
import com.isanz.spafy.common.entities.Album
import com.isanz.spafy.common.retrofit.SuccessResponse

data class AlbumResponse(
    @SerializedName("album") val albums: List<Album>
) : SuccessResponse(albums)