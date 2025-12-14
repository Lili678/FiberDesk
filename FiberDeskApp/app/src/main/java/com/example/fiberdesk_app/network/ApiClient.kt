package com.example.fiberdesk_app.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Detección automática de URL según entorno
    private fun getBaseUrl(): String {
        return NetworkConfig.getBaseUrl()
    }
    
    private var BASE_URL = getBaseUrl()
    
    // Token JWT que se inyecta en headers si existe
    private var token: String? = null

    fun setBaseUrl(url: String) {
        if (!url.endsWith("/")) {
            BASE_URL = "$url/"
        } else {
            BASE_URL = url
        }
        rebuildRetrofit()
    }
    
    fun refreshBaseUrl() {
        BASE_URL = getBaseUrl()
        rebuildRetrofit()
    }
    fun setToken(newToken: String?) {
        token = newToken
    }
    // Interceptor que añade Authorization si token != null
    private val authInterceptor = Interceptor { chain ->
        val reqBuilder = chain.request().newBuilder()
        token?.let {
            reqBuilder.addHeader("Authorization", "Bearer $it")
        }
        chain.proceed(reqBuilder.build())
    }
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private var okClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(logging)
        .build()
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    // Servicio expuesto
    @Volatile
    var apiService: ApiService = retrofit.create(ApiService::class.java)
        private set
    // Reconstruye retrofit si cambias la base URL
    private fun rebuildRetrofit() {
        okClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }
}

