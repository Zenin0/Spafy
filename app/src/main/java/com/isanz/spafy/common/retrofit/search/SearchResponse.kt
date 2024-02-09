package com.isanz.spafy.common.retrofit.search

import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.retrofit.SuccessResponse

data class SearchResponse(
    val canciones: List<Cancion>
) : SuccessResponse(canciones)