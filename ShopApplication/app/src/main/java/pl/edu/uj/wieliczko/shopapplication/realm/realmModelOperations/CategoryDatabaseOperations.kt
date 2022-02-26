package pl.edu.uj.wieliczko.shopapplication.realm.realmModelOperations

import io.realm.Realm
import pl.edu.uj.wieliczko.shopapplication.realm.RealmCustomConfiguration
import pl.edu.uj.wieliczko.shopapplication.realmModels.CategoryRealm
import pl.edu.uj.wieliczko.shopapplication.servicehandlers.getCategories

object CategoryDatabaseOperations {

    fun getRealmCategories(): List<CategoryRealm> {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        return realm.copyFromRealm(realm.where(CategoryRealm::class.java).findAll())
    }

    fun getRealmCategory(categoryName: String): CategoryRealm? {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        return realm.where(CategoryRealm::class.java).equalTo("categoryName", categoryName).findFirst()
    }

    fun insertRealmCategory(catName: String, prID: Int) {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        realm.executeTransaction { realmTransaction ->
            val categoryRealm = CategoryRealm(categoryName = catName, productID = prID)
            realmTransaction.insert(categoryRealm)
        }
    }

    fun synchronizeCategory() {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        realm.executeTransaction { realmTransaction ->
            realmTransaction.delete(CategoryRealm::class.java)
        }

        getCategories()
    }
}