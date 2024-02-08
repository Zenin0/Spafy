package com.isanz.spafy.common.retrofit.login

import com.google.gson.annotations.SerializedName
import com.isanz.spafy.common.utils.Constants

class UserLoginInfo(
    @SerializedName(Constants.EMAIL_PARAM) val email: String,
    @SerializedName(Constants.PASSWORD_PARAM) val pass: String
)