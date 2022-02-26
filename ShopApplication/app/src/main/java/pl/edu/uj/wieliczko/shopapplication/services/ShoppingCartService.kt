package pl.edu.uj.wieliczko.shopapplication.services

import pl.edu.uj.wieliczko.shopapplication.models.ShoppingCart
import retrofit2.Call
import retrofit2.http.*

interface ShoppingCartService {
    @GET("shoppingCart")
    fun getShoppingCarts(): Call<List<ShoppingCart>>

    @GET("shoppingCart/{id}")
    fun getShoppingCart(@Path("id") id: String): Call<ShoppingCart>

    @GET("shoppingCart/create_payment_intent/{nickname}")
    fun getPaymentIntentService(@Path("nickname") nickname: String): Call<String>

    @POST("shoppingCart")
    fun postShoppingCart(@Body shoppingCart: ShoppingCart): Call<ShoppingCart>

    @PUT("shoppingCart/{id}")
    fun putShoppingCart(@Path("id") id: String, @Body shoppingCart: ShoppingCart): Call<ShoppingCart>

    @DELETE("shoppingCart/{id}")
    fun deleteShoppingCart(@Path("id") id: String): Call<ShoppingCart>
}