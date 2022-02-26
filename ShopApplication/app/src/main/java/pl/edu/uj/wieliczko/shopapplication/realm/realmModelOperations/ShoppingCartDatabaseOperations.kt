package pl.edu.uj.wieliczko.shopapplication.realm.realmModelOperations

import io.realm.Realm
import pl.edu.uj.wieliczko.shopapplication.realm.RealmCustomConfiguration
import pl.edu.uj.wieliczko.shopapplication.realmModels.ShoppingCartRealm
import pl.edu.uj.wieliczko.shopapplication.servicehandlers.getShoppingCart

object ShoppingCartDatabaseOperations {
    fun getRealmShoppingCarts(): List<ShoppingCartRealm> {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        return realm.copyFromRealm(realm.where(ShoppingCartRealm::class.java).findAll())
    }

    fun getRealmShoppingCart(user: String): ShoppingCartRealm? {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        return realm.where(ShoppingCartRealm::class.java).equalTo("user", user).findFirst()
    }

    fun insertRealmShoppingCart(userName: String, prID: Int) {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        realm.executeTransaction { realmTransaction ->
            val shoppingCartRealm = ShoppingCartRealm(user = userName, product = prID)
            realmTransaction.insert(shoppingCartRealm)
        }
    }

    fun synchronizeShoppingCart(nickname: String) {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        realm.executeTransaction { realmTransaction ->
            realmTransaction.delete(ShoppingCartRealm::class.java)
        }

        getShoppingCart(nickname)
    }
}