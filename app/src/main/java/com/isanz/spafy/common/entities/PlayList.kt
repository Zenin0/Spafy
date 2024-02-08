package com.isanz.spafy.common.entities

import com.google.gson.annotations.SerializedName

data class PlayList(
    @SerializedName("id") var id: Int,
    @SerializedName("titulo") var titulo: String,
    @SerializedName("numeroCanciones") var numeroCanciones: Int,
    @SerializedName("fechaCreacion") var fechaCreacion: String
)