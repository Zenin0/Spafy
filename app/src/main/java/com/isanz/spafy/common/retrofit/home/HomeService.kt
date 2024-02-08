package com.isanz.spafy.common.retrofit.home

import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeService {
    @GET(Constants.BASE_URL + Constants.USER_PATH + "/{idUsuario}/" + Constants.PLAYLIST_PATH)
    suspend fun getUserPlaylists(@Path("idUsuario") idUsuario: Int): PlayListResponse
}