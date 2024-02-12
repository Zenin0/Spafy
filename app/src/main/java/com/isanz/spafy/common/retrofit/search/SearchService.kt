package com.isanz.spafy.common.retrofit.search

import com.isanz.spafy.common.entities.AnyadeCancionPlaylist
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface SearchService {

    @Headers("Content-Type: application/json")
    @GET(Constants.BASE_URL + Constants.CANCIONES_PATH)
    suspend fun getCanciones(): Response<List<Cancion>>


    @FormUrlEncoded
    @PUT(Constants.BASE_URL + Constants.PLAYLIST_PATH + "/{idPlaylist}/" + Constants.CANCION_PATH + "/{idCancion}")
    suspend fun addSongToPlaylist(
        @Path("idPlaylist") idPlaylist: Int,
        @Path("idCancion") idCancion: Int,
        @Field ("idUsuario") idUsuario: Int
    ): Response<AnyadeCancionPlaylist>


    @GET(Constants.BASE_URL + Constants.PLAYLIST_PATH + "/{id}/" + Constants.CANCIONES_PATH)
    suspend fun getCancionesPlaylist(@Path("id") id: Int): Response<List<Cancion>>

    @GET(Constants.BASE_URL + Constants.PLAYLIST_PATH + "/{id}")
    suspend fun getPlaylist(@Path("id") id: Int): Response<PlayList>


}