package pl.edu.uj.wieliczko.shopapplication.services

import pl.edu.uj.wieliczko.shopapplication.models.Wishlist
import retrofit2.Call
import retrofit2.http.*

interface WishlistService {
    @GET("wishlist")
    fun getWishlists(): Call<List<Wishlist>>

    @GET("wishlist/{id}")
    fun getWishlist(@Path("id") id: Int): Call<Wishlist>

    @POST("wishlist")
    fun postWishlist(@Body wishlist: Wishlist): Call<Wishlist>

    @PUT("wishlist/{id}")
    fun putWishlist(@Path("id") id: Int, @Body wishlist: Wishlist): Call<Wishlist>

    @DELETE("wishlist/{id}")
    fun deleteWishlist(@Path("id") id: Int): Call<Wishlist>
}