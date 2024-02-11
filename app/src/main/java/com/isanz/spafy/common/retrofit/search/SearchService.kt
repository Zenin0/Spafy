package com.isanz.spafy.common.retrofit.search

import com.isanz.spafy.common.entities.AnyadeCancionPlaylist
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface SearchService {

    @Headers("Content-Type: application/json")
    @GET(Constants.BASE_URL + Constants.CANCIONES_PATH)
    suspend fun getCanciones(): Response<List<Cancion>>

    @Headers("Content-Type: application/json")
    @PUT(Constants.BASE_URL + Constants.PLAYLIST_PATH + "/{idPlaylist}/" + Constants.CANCION_PATH + "/{idCancion}")
    suspend fun addSongToPlaylist(
        @Path("idPlaylist") idPlaylist: Int,
        @Path("idCancion") idCancion: Int,
        @Body body: UserIdInfo
    ): Response<AnyadeCancionPlaylist>

}