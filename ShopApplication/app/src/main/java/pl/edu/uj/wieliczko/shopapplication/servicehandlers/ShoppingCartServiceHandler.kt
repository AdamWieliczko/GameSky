package pl.edu.uj.wieliczko.shopapplication.servicehandlers

import android.util.Log
import io.realm.Realm
import pl.edu.uj.wieliczko.shopapplication.models.ShoppingCart
import pl.edu.uj.wieliczko.shopapplication.realm.RealmCustomConfiguration
import pl.edu.uj.wieliczko.shopapplication.realmModels.ShoppingCartRealm
import pl.edu.uj.wieliczko.shopapplication.services.getShoppingCartService
import pl.edu.uj.wieliczko.shopapplication.services.wrongCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getShoppingCarts() {
    getShoppingCartService()
        .getShoppingCarts()
        .enqueue(object : Callback<List<ShoppingCart>> {
            override fun onResponse(call: Call<List<ShoppingCart>>, response: Response<List<ShoppingCart>>) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        response.body()!!.forEach { i ->
                            val item = ShoppingCartRealm(i.user, i.product)
                            Realm.getInstance(RealmCustomConfiguration.providesRealmConfig()).executeTransactionAsync { realmTransaction ->
                                realmTransaction.insertOrUpdate(item)
                            }
                        }
                    }
                    Log.d("ShoppingCarts", response.body().toString())
                }
                else {
                    wrongCode("ShoppingCarts")
                }
            }

            override fun onFailure(call: Call<List<ShoppingCart>>, t: Throwable) {
                Log.d("ShoppingCarts", t.message.toString())
            }
        })
}

fun getShoppingCart(id: String) {
    getShoppingCartService()
        .getShoppingCart(id)
        .enqueue(object : Callback<ShoppingCart> {
            override fun onResponse(call: Call<ShoppingCart>, response: Response<ShoppingCart>) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        val i = response.body()!!
                        val item = ShoppingCartRealm(i.user, i.product)
                        Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())
                            .executeTransactionAsync { realmTransaction ->
                                realmTransaction.insertOrUpdate(item)
                            }
                    }
                    Log.d("ShoppingCart", response.body().toString())
                }
                else {
                    wrongCode("ShoppingCart")
                }
            }

            override fun onFailure(call: Call<ShoppingCart>, t: Throwable) {
                Log.d("ShoppingCart", t.message.toString())
            }
        })
}

fun postShoppingCart(shoppingCart: ShoppingCart) {
    getShoppingCartService()
        .postShoppingCart(shoppingCart)
        .enqueue(object : Callback<ShoppingCart> {
            override fun onResponse(call: Call<ShoppingCart>, response: Response<ShoppingCart>) {
                if (response.code() != 201) {
                    wrongCode("ShoppingCart")
                }
            }

            override fun onFailure(call: Call<ShoppingCart>, t: Throwable) {
                Log.d("ShoppingCart", t.message.toString())
            }
        })
}

fun putShoppingCart(id: String, shoppingCart: ShoppingCart) {
    getShoppingCartService()
        .putShoppingCart(id, shoppingCart)
        .enqueue(object : Callback<ShoppingCart> {
            override fun onResponse(call: Call<ShoppingCart>, response: Response<ShoppingCart>) {
                if (response.code() != 200) {
                    wrongCode("ShoppingCart")
                }
            }

            override fun onFailure(call: Call<ShoppingCart>, t: Throwable) {
                Log.d("ShoppingCart", t.message.toString())
            }
        })
}

fun deleteShoppingCart(id: String) {
    getShoppingCartService()
        .deleteShoppingCart(id)
        .enqueue(object : Callback<ShoppingCart> {
            override fun onResponse(call: Call<ShoppingCart>, response: Response<ShoppingCart>) {
                if (response.code() != 200) {
                    wrongCode("ShoppingCart")
                }
            }

            override fun onFailure(call: Call<ShoppingCart>, t: Throwable) {
                Log.d("ShoppingCart", t.message.toString())
            }
        })
}