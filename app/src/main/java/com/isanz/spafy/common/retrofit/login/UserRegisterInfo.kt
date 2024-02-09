package com.isanz.spafy.common.retrofit.login

import com.google.gson.annotations.SerializedName
import com.isanz.spafy.common.utils.Constants

class UserRegisterInfo(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String,
    @SerializedName("genero") val gender: String,
    @SerializedName("fechaNacimiento") val birthDate: String,
    @SerializedName("pais") val country: String,
    @SerializedName("codigoPostal") val postalCode: String,
    @SerializedName("cancion") val song: List<String> = emptyList(),
    @SerializedName("podcast") val podcast: List<String> = emptyList(),
    @SerializedName("album") val album: List<String> = emptyList(),
    @SerializedName("playlist") val playlist: List<String> = emptyList()
)