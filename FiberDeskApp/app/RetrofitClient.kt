package com.example.fiberdesk_app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // CAMBIA ESTO por la IP de tu servidor real o "http://10.0.2.2:PUERTO/" para local
    private const val BASE_URL = "http://10.0.2.2:3000/" 

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}