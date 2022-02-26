package pl.edu.uj.wieliczko.shopapplication.realmModels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class UserRealm(
    @PrimaryKey
    var nickname: String = "",
    @Required
    var name: String = "",
    @Required
    var surname: String = "",
    @Required
    var password: String = ""
): RealmObject()
