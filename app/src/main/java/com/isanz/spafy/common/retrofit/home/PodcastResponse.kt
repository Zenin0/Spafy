package com.isanz.spafy.common.retrofit.home

import com.google.gson.annotations.SerializedName
import com.isanz.spafy.common.entities.Podcast
import com.isanz.spafy.common.retrofit.SuccessResponse

data class PodcastResponse(
    @SerializedName("podcast") val podcast: List<Podcast>
) : SuccessResponse(podcast)