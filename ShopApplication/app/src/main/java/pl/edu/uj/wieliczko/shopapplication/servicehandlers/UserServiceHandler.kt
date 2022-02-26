package pl.edu.uj.wieliczko.shopapplication.servicehandlers

import android.util.Log
import io.realm.Realm
import pl.edu.uj.wieliczko.shopapplication.models.User
import pl.edu.uj.wieliczko.shopapplication.realm.RealmCustomConfiguration
import pl.edu.uj.wieliczko.shopapplication.realmModels.UserRealm
import pl.edu.uj.wieliczko.shopapplication.services.getUserService
import pl.edu.uj.wieliczko.shopapplication.services.wrongCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getUsers() {
    getUserService()
        .getUsers()
        .enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        response.body()!!.forEach { i ->
                            val item = UserRealm(i.nickname, i.name, i.surname, i.password)
                            Realm.getInstance(RealmCustomConfiguration.providesRealmConfig()).executeTransactionAsync { realmTransaction ->
                                realmTransaction.insertOrUpdate(item)
                            }
                        }
                    }

                    Log.d("Users", response.body().toString())
                }
                else {
                    wrongCode("Users")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("Users", t.message.toString())
            }
        })
}

fun getUser(nickname: String) {
    getUserService()
        .getUser(nickname)
        .enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        val i = response.body()!!
                        val item = UserRealm(i.nickname, i.name, i.surname, i.password)
                        Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())
                            .executeTransaction { realmTransaction ->
                                realmTransaction.insertOrUpdate(item)
                            }
                    }
                    Log.d("User", response.body().toString())
                }
                else {
                    wrongCode("User")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("User", t.message.toString())
            }
        })
}

fun postUser(user: User) {
    getUserService()
        .postUser(user)
        .enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() != 201) {
                    wrongCode("User")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("User", t.message.toString())
            }
        })
}

fun postAndAddToRealmDB(user: User) {
    getUserService()
        .postUser(user)
        .enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() != 201) {
                    wrongCode("User")
                }
                else {
                    val item = UserRealm(user.nickname, user.name, user.surname, user.password)
                    Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())
                        .executeTransaction { realmTransaction ->
                            realmTransaction.insertOrUpdate(item)
                        }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("User", t.message.toString())
            }
        })
}

fun putUser(nickname: String, user: User) {
    getUserService()
        .putUser(nickname, user)
        .enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() != 200) {
                    wrongCode("User")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("User", t.message.toString())
            }
        })
}

fun deleteUser(nickname: String) {
    getUserService()
        .deleteUser(nickname)
        .enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() != 200) {
                    wrongCode("User")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("User", t.message.toString())
            }
        })
}