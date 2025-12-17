package com.example.fiberdesk_app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // IMPORTANTE:
    // Si usas el Emulador de Android Studio, usa "http://10.0.2.2:3000/api/"
    // Si usas un celular físico, usa la IP de tu PC, ej: "http://192.168.1.50:3000/api/"
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}