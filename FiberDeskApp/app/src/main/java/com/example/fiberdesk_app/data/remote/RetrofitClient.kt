package com.example.fiberdesk_app.data.remote

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    // Note: backend mounts inventario routes under /api/inventario
    private const val BASE_URL = "http://192.168.1.104:3000/api/"

    private val logging = HttpLoggingInterceptor { message ->
        Log.d("Retrofit", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // Comentado temporalmente - enfocado en módulo de pagos
    /*
    val inventarioApi: InventarioApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InventarioApiService::class.java)
    }

    val instalacionApi: InstalacionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InstalacionApiService::class.java)
    }
    */
}

