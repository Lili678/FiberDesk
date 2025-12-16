package com.example.fiberdesk_app

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TicketService {

    // Crear ticket
    @POST("api/tickets")
    fun crearTicket(
        @Body ticket: Ticket
    ): Call<Map<String, Any>>

    // Listar tickets
    @GET("api/tickets")
    fun getTickets(): Call<List<Ticket>>

    // Buscar tickets
    @GET("api/tickets/search")
    fun searchTickets(
        @Query("query") query: String
    ): Call<List<Ticket>>


    @PUT("api/tickets/archive/{folio}")
    fun archivarTicket(
        @Path("folio") folio: String
    ): Call<Map<String, Any>>

    @PUT("api/tickets/unarchive/{folio}")
    fun desarchivarTicket(
        @Path("folio") folio: String
    ): Call<Map<String, Any>>


    // Tickets archivados
    @GET("api/tickets/archived")
    fun getArchivedTickets(): Call<List<Ticket>>


}
