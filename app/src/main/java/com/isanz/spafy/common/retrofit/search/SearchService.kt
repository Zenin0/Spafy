package com.isanz.spafy.common.retrofit.search

import com.isanz.spafy.common.utils.Constants
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SearchService {

    @Headers("Content-Type: application/json")
    @POST(Constants.BASE_URL + Constants.CANCIONES_PATH)
    suspend fun getCanciones(): SearchResponse

}