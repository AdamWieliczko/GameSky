package pl.edu.uj.wieliczko.shopapplication.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class CategoryRealm(
    @Required
    var categoryName: String = "",
    @PrimaryKey
    var productID: Int = -1
): RealmObject()