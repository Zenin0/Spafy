package com.isanz.spafy.common.retrofit.search

import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface SearchService {

    @Headers("Content-Type: application/json")
    @GET(Constants.BASE_URL + Constants.CANCIONES_PATH)
    suspend fun getCanciones(): Response<List<Cancion>>

}