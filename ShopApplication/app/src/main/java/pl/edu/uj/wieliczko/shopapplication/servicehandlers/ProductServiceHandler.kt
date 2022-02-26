package pl.edu.uj.wieliczko.shopapplication.servicehandlers

import android.util.Log
import io.realm.Realm
import pl.edu.uj.wieliczko.shopapplication.models.Product
import pl.edu.uj.wieliczko.shopapplication.realm.RealmCustomConfiguration
import pl.edu.uj.wieliczko.shopapplication.realmModels.ProductRealm
import pl.edu.uj.wieliczko.shopapplication.services.getProductService
import pl.edu.uj.wieliczko.shopapplication.services.wrongCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getProducts() {
    getProductService()
        .getProducts()
        .enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        response.body()!!.forEach { i ->
                            val item = ProductRealm(i.id, i.name, i.description, i.price, i.isAvailable)
                            Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())
                                .executeTransactionAsync { realmTransaction ->
                                    realmTransaction.insertOrUpdate(item)
                                }
                        }
                    }

                    Log.d("Products", response.body().toString())
                }
                else {
                    wrongCode("Products")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("Products", t.message.toString())
            }
        })
}

fun getProduct(id: Int) {
    getProductService()
        .getProduct(id)
        .enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        val i = response.body()!!
                        val item = ProductRealm(i.id, i.name, i.description, i.price, i.isAvailable)
                        Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())
                            .executeTransactionAsync { realmTransaction ->
                                realmTransaction.insertOrUpdate(item)
                            }
                    }

                    Log.d("Product", response.body().toString())
                }
                else {
                    wrongCode("Product")
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.d("Product", t.message.toString())
            }
        })
}

fun postProduct(product: Product) {
    getProductService()
        .postProduct(product)
        .enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.code() != 201) {
                    wrongCode("Product")
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.d("Product", t.message.toString())
            }
        })
}

fun putProduct(id: Int, product: Product) {
    getProductService()
        .putProduct(id, product)
        .enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.code() != 200) {
                    wrongCode("Product")
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.d("Product", t.message.toString())
            }
        })
}

fun deleteProduct(id: Int) {
    getProductService()
        .deleteProduct(id)
        .enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.code() != 200) {
                    wrongCode("Product")
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.d("Product", t.message.toString())
            }
        })
}