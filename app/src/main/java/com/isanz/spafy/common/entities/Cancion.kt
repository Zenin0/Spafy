package com.isanz.spafy.common.entities

import com.google.gson.annotations.SerializedName

data class Cancion(
    @SerializedName("id") var id: Int,
    @SerializedName("titulo") var titulo: String,
    @SerializedName("duracion") var duracion: Int,
    @SerializedName("ruta") var ruta: String?,
    @SerializedName("numeroReproducciones") var numeroReproducciones: Int,
    @SerializedName("album") var album: Album
)