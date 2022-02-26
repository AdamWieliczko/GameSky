package pl.edu.uj.wieliczko.shopapplication.services

import pl.edu.uj.wieliczko.shopapplication.models.User
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("user")
    fun getUsers(): Call<List<User>>

    @GET("user/{id}")
    fun getUser(@Path("id") nickname: String): Call<User>

    @POST("user")
    fun postUser(@Body user: User): Call<User>

    @PUT("user/{id}")
    fun putUser(@Path("id") nickname: String, @Body user: User): Call<User>

    @DELETE("user/{id}")
    fun deleteUser(@Path("id") nickname: String): Call<User>
}