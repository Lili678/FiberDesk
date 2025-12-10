package com.example.fiberdesk_app.network

import com.example.fiberdesk_app.models.LoginRequest
import com.example.fiberdesk_app.models.LoginResponse
import com.example.fiberdesk_app.models.RegisterRequest
import com.example.fiberdesk_app.models.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/registro")
    suspend fun registro(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("auth/me")
    suspend fun getPerfil(): Response<Usuario>
}
