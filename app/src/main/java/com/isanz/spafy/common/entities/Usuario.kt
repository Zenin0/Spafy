package com.isanz.spafy.common.entities

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String
)