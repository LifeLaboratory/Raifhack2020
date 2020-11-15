package ru.lifelaboratory.raifhack2020.utils

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class Client(
    val id: Int = 0,
    val name: String = "",
    val surname: String = "",
    val number: Long = 0,
    val address: String = "",
    val city: String = "",
    val email: String = ""
)

data class Order(
    val id: Int = 0,
    val cost: Double = 0.0,
    var number_courier: Int = 0,
    var number_client: Int = 0,
    var status_order: Boolean = false,
    var check: String = "No",
    var rating: Int = 0,
    val address: String = "",
    var qr_code: String = "http://yandex.ru"
)

data class Location(
    val lat: Double = 0.0,
    val lon: Double = 0.0
)

var idClient: Int = 0
var qrUrlClient: String = ""
var addressClient: String = ""

interface ServerAPI {

    @GET("/api/client/{id}")
    fun getClient(@Path("id") id: Int): Call<Client>

    @GET("/api/courier/orders/{id}")
    fun getOrders(@Path("id") id: Int): Call<List<Order>>

    @POST("/api/courier/orders/{id}")
    fun sendLocation(@Path("id") id: Int, @Body location: Location): Call<Boolean>

    companion object Factory {
        fun create(): ServerAPI {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://46.148.224.125:7070")
                .build()

            return retrofit.create(ServerAPI::class.java)
        }
    }

}