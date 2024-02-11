package com.isanz.spafy.common.entities

import com.google.gson.annotations.SerializedName
import java.util.Date

data class AnyadeCancionPlaylist(
    @SerializedName("fechaAnyadida") val fechaAnyadida: Date,
    @SerializedName("playlist") val playlist: PlayList,
    @SerializedName("cancion") val cancion: Cancion,
    @SerializedName("usuario") val usuario: Usuario
)