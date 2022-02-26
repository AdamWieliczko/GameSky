package pl.edu.uj.wieliczko.shopapplication.realm.realmModelOperations

import io.realm.Realm
import pl.edu.uj.wieliczko.shopapplication.realm.RealmCustomConfiguration
import pl.edu.uj.wieliczko.shopapplication.realmModels.UserRealm
import pl.edu.uj.wieliczko.shopapplication.servicehandlers.getUsers

object UserDatabaseOperations {
    fun getRealmUsers(): List<UserRealm> {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        return realm.copyFromRealm(realm.where(UserRealm::class.java).findAll())
    }

    fun getRealmUser(nick: String): UserRealm? {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        return realm.where(UserRealm::class.java).equalTo("nickname", nick).findFirst()
    }

    fun insertRealmUser(nickname: String, name: String, surname: String, password: String) {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        realm.executeTransaction { realmTransaction ->
            val userRealm = UserRealm(nickname = nickname, name = name, surname = surname, password = password)
            realmTransaction.insert(userRealm)
        }
    }

    fun synchronizeUser() {
        val realm = Realm.getInstance(RealmCustomConfiguration.providesRealmConfig())

        realm.executeTransaction { realmTransaction ->
            realmTransaction.delete(UserRealm::class.java)
        }
        getUsers()
    }
}
