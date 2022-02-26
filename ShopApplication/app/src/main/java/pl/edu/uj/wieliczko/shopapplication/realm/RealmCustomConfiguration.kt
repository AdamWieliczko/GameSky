package pl.edu.uj.wieliczko.shopapplication.realm

import io.realm.RealmConfiguration

object RealmCustomConfiguration {
    private val realmVersion = 1L

    fun providesRealmConfig(): RealmConfiguration =
        RealmConfiguration.Builder()
            .name("ShopLocalRealmDatabase")
            .schemaVersion(realmVersion)
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()
}