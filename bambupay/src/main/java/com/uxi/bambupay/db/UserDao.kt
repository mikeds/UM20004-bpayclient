package com.uxi.bambupay.db

import com.uxi.bambupay.model.User
import io.realm.Realm

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
class UserDao(val realm: Realm) {

    fun copyOrUpdate(obj: User) {
        realm.executeTransaction { realm1 ->
            realm1.copyToRealmOrUpdate(obj)
        }
    }

    fun getCurrentUser(): User? {
        val currentUser = realm.where(User::class.java).findFirst()
        currentUser?.let {
            return realm.copyFromRealm(currentUser)
        }
        return null
    }

}