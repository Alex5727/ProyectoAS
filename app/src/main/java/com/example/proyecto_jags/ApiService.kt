package com.example.proyecto_jags

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/Auth/LogIn")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>
}