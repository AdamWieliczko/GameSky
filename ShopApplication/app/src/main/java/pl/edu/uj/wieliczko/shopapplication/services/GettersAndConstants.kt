package pl.edu.uj.wieliczko.shopapplication.services

import android.util.Log
import okhttp3.OkHttpClient
import pl.edu.uj.wieliczko.shopapplication.realm.realmModelOperations.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BaseUrl = "https://a8e7-195-150-224-60.ngrok.io"

fun wrongCode(tag: String) {
    Log.d(tag, "wrong response code")
}

fun refreshData(nickname: String?){
    CategoryDatabaseOperations.synchronizeCategory()
    ProductDatabaseOperations.synchronizeProducts()
    if(!nickname.isNullOrEmpty()) {
        ShoppingCartDatabaseOperations.synchronizeShoppingCart(nickname)
    }
    UserDatabaseOperations.synchronizeUser()
    WishlistDatabaseOperations.synchronizeWishlist()
}

fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().build())
        .build()
}

fun getCategoryService(): CategoryService {
    return getRetrofit().create(CategoryService::class.java)
}

fun getProductService(): ProductService {
    return getRetrofit().create(ProductService::class.java)
}

fun getShoppingCartService(): ShoppingCartService {
    return getRetrofit().create(ShoppingCartService::class.java)
}

fun getUserService(): UserService {
    return getRetrofit().create(UserService::class.java)
}

fun getWishlistService(): WishlistService {
    return getRetrofit().create(WishlistService::class.java)
}