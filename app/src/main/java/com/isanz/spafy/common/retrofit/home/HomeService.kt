package com.isanz.spafy.common.retrofit.home

import com.isanz.spafy.common.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeService {
    @GET(Constants.BASE_URL + Constants.USER_PATH + "/{id}/" + Constants.PLAYLISTS_PATH)
    suspend fun getUserPlaylists(@Path("id") id: Int): PlayListResponse
    @GET(Constants.BASE_URL + Constants.USER_PATH + "/{id}/" + Constants.PODCAST_PATH)
    suspend fun getUserPodcast(@Path("id") id: Int): PodcastResponse

    @GET(Constants.BASE_URL + Constants.USER_PATH + "/{id}/" + Constants.ALBUM_PATH)
    suspend fun getUserAlbums(@Path("id") id: Int): AlbumResponse


}