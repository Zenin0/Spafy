package com.isanz.spafy.common.retrofit.library

import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LibraryService {

    @Headers("Content-Type: application/json")
    @POST(Constants.BASE_URL +  Constants.PLAYLISTS_PATH)
    suspend fun createPlaylist(@Body data: PostPlaylist) : Response<PlayList>
}