package pl.edu.uj.wieliczko.shopapplication.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ShoppingCartRealm(
    @PrimaryKey
    var user: String = "",
    var product: Int = -1
): RealmObject()