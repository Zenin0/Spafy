package com.isanz.spafy.common.retrofit.home

import com.google.gson.annotations.SerializedName
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.retrofit.SuccessResponse

data class PlayListResponse(
    @SerializedName("playlist") val playlists: List<PlayList>
) : SuccessResponse(playlists)