package com.isanz.spafy.common.retrofit.library

import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.entities.Usuario
import com.isanz.spafy.common.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface LibraryService {

    @Headers("Content-Type: application/json")
    @POST(Constants.BASE_URL +  Constants.PLAYLISTS_PATH)
    suspend fun createPlaylist(@Body data: PostPlaylist) : Response<PlayList>

    @Headers("Content-Type: application/json")
    @DELETE(Constants.BASE_URL +  Constants.USER_PATH + "/{id}")
    suspend fun closeAccount(@Path("id") id: Int ) : Response<Usuario>


    @Headers("Content-Type: application/json")
    @GET(Constants.BASE_URL +  Constants.USER_PATH + "/{id}")
    suspend fun getUser(@Path("id") id: Int ) : Response<Usuario>
}