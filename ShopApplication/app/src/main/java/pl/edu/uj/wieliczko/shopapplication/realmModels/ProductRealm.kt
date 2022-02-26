package pl.edu.uj.wieliczko.shopapplication.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class ProductRealm(
    @PrimaryKey
    var id: Int = -1,
    @Required
    var name: String = "",
    @Required
    var description: String = "",
    var price: Int = -1,
    var isAvailable: Boolean = true
): RealmObject()