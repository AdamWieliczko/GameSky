package pl.edu.uj.wieliczko.shopapplication.services

import pl.edu.uj.wieliczko.shopapplication.models.Category
import retrofit2.Call
import retrofit2.http.*

interface CategoryService {
    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("category/{id}")
    fun getCategory(@Path("id") id: Int): Call<Category>

    @POST("category")
    fun postCategory(@Body category: Category): Call<Category>

    @PUT("category/{id}")
    fun putCategory(@Path("id") id: Int, @Body category: Category): Call<Category>

    @DELETE("category/{id}")
    fun deleteCategory(@Path("id") id: Int): Call<Category>
}