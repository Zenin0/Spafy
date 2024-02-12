package com.isanz.spafy.common.retrofit.library

import com.google.gson.annotations.SerializedName
import com.isanz.spafy.common.utils.Constants

class PostPlaylist(
    @SerializedName(Constants.TITTLE_PARAM) val titulo: String,
    @SerializedName(Constants.USURIO_PARAM) val usuarioId: Int
)
