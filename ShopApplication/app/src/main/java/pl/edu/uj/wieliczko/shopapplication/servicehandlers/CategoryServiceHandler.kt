package pl.edu.uj.wieliczko.shopapplication.servicehandlers

import android.util.Log
import io.realm.Realm
import pl.edu.uj.wieliczko.shopapplication.models.Category
import pl.edu.uj.wieliczko.shopapplication.realm.RealmCustomConfiguration
import pl.edu.uj.wieliczko.shopapplication.realmModels.CategoryRealm
import pl.edu.uj.wieliczko.shopapplication.services.getCategoryService
import pl.edu.uj.wieliczko.shopapplication.services.wrongCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getCategories() {
    getCategoryService()
        .getCategories()
        .enqueue(object : Callback<List<Category>> {
        override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
            if (response.code() == 200) {
                if(response.body() != null) {
                    response.body()!!.forEach { i ->
                        val item = CategoryRealm(i.categoryName, i.productID)
                        Realm.getInstance(RealmCustomConfiguration.providesRealmConfig()).executeTransactionAsync { realmTransaction ->
                            realmTransaction.insertOrUpdate(item)
                        }
                    }
                }


                Log.d("Categories", response.body().toString())
            }
            else {
                wrongCode("Categories")
            }
        }

        override fun onFailure(call: Call<List<Category>>, t: Throwable) {
            Log.d("Categories", t.message.toString())
        }
    })
}

fun getCategory(id: Int) {
    getCategoryService()
        .getCategory(id)
        .enqueue(object : Callback<Category> {
        override fun onResponse(call: Call<Category>, response: Response<Category>) {
            if (response.code() == 200) {
                if(response.body() != null) {
                    val i = response.body()!!
                    val item = CategoryRealm(i.categoryName, i.productID)
                    Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())
                        .executeTransactionAsync { realmTransaction ->
                            realmTransaction.insertOrUpdate(item)
                        }
                }

                Log.d("Category", response.body().toString())
            }
            else {
                wrongCode("Category")
            }
        }

        override fun onFailure(call: Call<Category>, t: Throwable) {
            Log.d("Category", t.message.toString())
        }
    })
}

fun postCategory(category: Category) {
    getCategoryService()
        .postCategory(category)
        .enqueue(object : Callback<Category> {
        override fun onResponse(call: Call<Category>, response: Response<Category>) {
            if (response.code() != 201) {
                wrongCode("Category")
            }
        }

        override fun onFailure(call: Call<Category>, t: Throwable) {
            Log.d("Category", t.message.toString())
        }
    })
}

fun putCategory(id: Int, category: Category) {
    getCategoryService()
        .putCategory(id, category)
        .enqueue(object : Callback<Category> {
        override fun onResponse(call: Call<Category>, response: Response<Category>) {
            if (response.code() != 200) {
                wrongCode("Category")
            }
        }

        override fun onFailure(call: Call<Category>, t: Throwable) {
            Log.d("Category", t.message.toString())
        }
    })
}

fun deleteCategory(id: Int) {
    getCategoryService()
        .deleteCategory(id)
        .enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if (response.code() != 200) {
                    wrongCode("Category")
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                Log.d("Category", t.message.toString())
            }
        })
}