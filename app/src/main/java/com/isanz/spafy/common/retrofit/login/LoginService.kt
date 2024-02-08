package com.isanz.spafy.common.retrofit.login

import com.isanz.spafy.common.utils.Constants
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {

    @Headers("Content-Type: application/json")
    @POST(Constants.BASE_URL + Constants.LOGIN_PATH)
    suspend fun logUser(@Body data: UserLoginInfo): LoginResponse

}