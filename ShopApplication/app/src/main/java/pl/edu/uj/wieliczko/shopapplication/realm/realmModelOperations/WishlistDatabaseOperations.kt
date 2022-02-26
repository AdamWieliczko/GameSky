package pl.edu.uj.wieliczko.shopapplication.realm.realmModelOperations

import io.realm.Realm
import pl.edu.uj.wieliczko.shopapplication.realm.RealmCustomConfiguration
import pl.edu.uj.wieliczko.shopapplication.realmModels.WishlistRealm
import pl.edu.uj.wieliczko.shopapplication.servicehandlers.getWishlist

object WishlistDatabaseOperations {
    fun getRealmWishlists(): List<WishlistRealm> {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        return realm.copyFromRealm(realm.where(WishlistRealm::class.java).findAll())
    }

    fun getRealmWishlist(user: String): WishlistRealm? {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        return realm.where(WishlistRealm::class.java).equalTo("user", user).findFirst()
    }

    fun insertRealmWishlist(user: String, productID: Int) {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        realm.executeTransaction { realmTransaction ->
            val wishlistRealm = WishlistRealm(user = user, product = productID)
            realmTransaction.insert(wishlistRealm)
        }
    }

    fun synchronizeWishlist() {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        realm.executeTransaction { realmTransaction ->
            realmTransaction.delete(WishlistRealm::class.java)
        }

        getWishlist()
    }
}