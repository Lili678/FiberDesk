package com.example.fiberdesk_app.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Note: backend mounts inventario routes under /api/inventario
    private const val BASE_URL = "http://192.168.1.68:3000/api/inventario/"

    val inventarioApi: InventarioApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InventarioApi::class.java)
    }
}
