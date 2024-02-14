package com.isanz.spafy.common.entities

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("id") var id: Int,
    @SerializedName("titulo") var titulo: String
)

