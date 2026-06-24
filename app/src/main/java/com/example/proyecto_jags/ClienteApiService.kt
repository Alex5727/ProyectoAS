package com.example.proyecto_jags

import retrofit2.Call
import retrofit2.http.*

interface ClienteApiService {

    @GET("api/Client/Get/Clientes")
    fun obtenerClientes(): Call<List<Cliente>>

    @POST("api/Client/Create/Cliente")
    fun insertarCliente(
        @Body cliente: Cliente
    ): Call<Any>

    @PUT("api/Client/Update/Cliente")
    fun actualizarCliente(
        @Body cliente: Cliente
    ): Call<Any>

    @DELETE("api/Client/{clave}")
    fun eliminarCliente(
        @Path("clave") clave:String
    ): Call<Any>
}