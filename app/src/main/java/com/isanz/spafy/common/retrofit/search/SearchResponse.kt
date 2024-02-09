package com.isanz.spafy.common.retrofit.search

import com.google.gson.annotations.SerializedName
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.retrofit.SuccessResponse

data class SearchResponse(
    @SerializedName("canciones") val canciones: List<Cancion>
) : SuccessResponse(canciones)