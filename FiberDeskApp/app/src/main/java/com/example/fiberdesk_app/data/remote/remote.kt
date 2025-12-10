package com.example.fiberdesk_app.data.remote

import com.example.fiberdesk_app.data.model.*
import com.example.fiberdesk_app.utils.constants.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

// API Service Interface
interface PagosApiService {
    
    @GET("api/pagos")
    suspend fun obtenerPagos(): Response<List<Pago>>
    
    @GET("api/pagos/{id}")
    suspend fun obtenerPagoPorId(@Path("id") id: String): Response<Pago>
    
    @GET("api/pagos/usuario/{usuarioId}")
    suspend fun obtenerPagosPorUsuario(@Path("usuarioId") usuarioId: String): Response<List<Pago>>
    
    @POST("api/pagos")
    suspend fun crearPago(@Body pago: CrearPagoRequest): Response<PagoResponse>
    
    @PUT("api/pagos/{id}")
    suspend fun actualizarPago(
        @Path("id") id: String,
        @Body pago: ActualizarPagoRequest
    ): Response<PagoResponse>
    
    @DELETE("api/pagos/{id}")
    suspend fun eliminarPago(@Path("id") id: String): Response<Map<String, String>>
}

// Retrofit Client singleton
object RetrofitClient {
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val pagosApi: PagosApiService = retrofit.create(PagosApiService::class.java)
}