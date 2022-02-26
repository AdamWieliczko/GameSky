package pl.edu.uj.wieliczko.shopapplication.servicehandlers

import android.util.Log
import io.realm.Realm
import pl.edu.uj.wieliczko.shopapplication.models.Wishlist
import pl.edu.uj.wieliczko.shopapplication.realm.RealmCustomConfiguration
import pl.edu.uj.wieliczko.shopapplication.realmModels.WishlistRealm
import pl.edu.uj.wieliczko.shopapplication.services.getWishlistService
import pl.edu.uj.wieliczko.shopapplication.services.wrongCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun getWishlist() {
    getWishlistService()
        .getWishlists()
        .enqueue(object : Callback<List<Wishlist>> {
            override fun onResponse(call: Call<List<Wishlist>>, response: Response<List<Wishlist>>) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        response.body()!!.forEach { i ->
                            val item = WishlistRealm(i.user, i.product)
                            Realm.getInstance(RealmCustomConfiguration.providesRealmConfig()).executeTransactionAsync { realmTransaction ->
                                realmTransaction.insertOrUpdate(item)
                            }
                        }
                    }

                    Log.d("Wishlists", response.body().toString())
                }
                else {
                    wrongCode("Wishlists")
                }
            }

            override fun onFailure(call: Call<List<Wishlist>>, t: Throwable) {
                Log.d("Wishlists", t.message.toString())
            }
        })
}

fun getWishlist(id: Int) {
    getWishlistService()
        .getWishlist(id)
        .enqueue(object : Callback<Wishlist> {
            override fun onResponse(call: Call<Wishlist>, response: Response<Wishlist>) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        val i = response.body()!!
                        val item = WishlistRealm(i.user, i.product)
                        Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())
                            .executeTransactionAsync { realmTransaction ->
                                realmTransaction.insertOrUpdate(item)
                            }
                    }

                    Log.d("Wishlist", response.body().toString())
                }
                else {
                    wrongCode("Wishlist")
                }
            }

            override fun onFailure(call: Call<Wishlist>, t: Throwable) {
                Log.d("Wishlist", t.message.toString())
            }
        })
}

fun postWishlist(wishlist: Wishlist) {
    getWishlistService()
        .postWishlist(wishlist)
        .enqueue(object : Callback<Wishlist> {
            override fun onResponse(call: Call<Wishlist>, response: Response<Wishlist>) {
                if (response.code() != 201) {
                    wrongCode("Wishlist")
                }
            }

            override fun onFailure(call: Call<Wishlist>, t: Throwable) {
                Log.d("Wishlist", t.message.toString())
            }
        })
}

fun putWishlist(id: Int, wishlist: Wishlist) {
    getWishlistService()
        .putWishlist(id, wishlist)
        .enqueue(object : Callback<Wishlist> {
            override fun onResponse(call: Call<Wishlist>, response: Response<Wishlist>) {
                if (response.code() != 200) {
                    wrongCode("Wishlist")
                }
            }

            override fun onFailure(call: Call<Wishlist>, t: Throwable) {
                Log.d("Wishlist", t.message.toString())
            }
        })
}

fun deleteWishlist(id: Int) {
    getWishlistService()
        .deleteWishlist(id)
        .enqueue(object : Callback<Wishlist> {
            override fun onResponse(call: Call<Wishlist>, response: Response<Wishlist>) {
                if (response.code() != 200) {
                    wrongCode("Wishlist")
                }
            }

            override fun onFailure(call: Call<Wishlist>, t: Throwable) {
                Log.d("Wishlist", t.message.toString())
            }
        })
}