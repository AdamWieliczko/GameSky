package pl.edu.uj.wieliczko.shopapplication.services

import pl.edu.uj.wieliczko.shopapplication.models.Product
import retrofit2.Call
import retrofit2.http.*

interface ProductService {
    @GET("product")
    fun getProducts(): Call<List<Product>>

    @GET("product/{id}")
    fun getProduct(@Path("id") id: Int): Call<Product>

    @POST("product")
    fun postProduct(@Body product: Product): Call<Product>

    @PUT("product/{id}")
    fun putProduct(@Path("id") id: Int, @Body product: Product): Call<Product>

    @DELETE("product/{id}")
    fun deleteProduct(@Path("id") id: Int): Call<Product>
}