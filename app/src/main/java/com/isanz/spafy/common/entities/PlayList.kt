package com.isanz.spafy.common.entities

import com.google.gson.annotations.SerializedName

data class PlayList(
    @SerializedName("id") val id: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("numeroCanciones") val numeroCanciones: Int,
    @SerializedName("fechaCreacion") val fechaCreacion: String
)