package pl.edu.uj.wieliczko.shopapplication.realm.realmModelOperations

import io.realm.Realm
import pl.edu.uj.wieliczko.shopapplication.realm.RealmCustomConfiguration
import pl.edu.uj.wieliczko.shopapplication.realmModels.ProductRealm
import pl.edu.uj.wieliczko.shopapplication.servicehandlers.getProducts

object ProductDatabaseOperations {
    fun getRealmProducts(): List<ProductRealm> {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        return realm.copyFromRealm(realm.where(ProductRealm::class.java).findAll())
    }

    fun getRealmProduct(productID: Int): ProductRealm? {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        return realm.where(ProductRealm::class.java).equalTo("id", productID).findFirst()
    }

    fun insertRealmProduct(productID: Int, productName: String, productDesc: String, productPrice: Int, productAvailability: Boolean) {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        realm.executeTransaction { realmTransaction ->
            val productRealm = ProductRealm(id = productID, name = productName, description = productDesc, price = productPrice, isAvailable = productAvailability)
            realmTransaction.insert(productRealm)
        }
    }

    fun synchronizeProducts() {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        realm.executeTransaction { realmTransaction ->
            realmTransaction.delete(ProductRealm::class.java)
        }

        getProducts()
    }
}