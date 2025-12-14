package com.example.fiberdesk_app.data.remote

import android.util.Log
import com.example.fiberdesk_app.network.NetworkConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    // Detección automática de URL según entorno (emulador o dispositivo físico)
    private val BASE_URL = NetworkConfig.getBaseUrl()

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

