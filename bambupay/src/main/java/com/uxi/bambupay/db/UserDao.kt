package com.uxi.bambupay.db

import com.uxi.bambupay.model.Balance
import com.uxi.bambupay.model.Transaction
import com.uxi.bambupay.model.User
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

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

    fun deleteAll() {
        realm.executeTransaction {
            it.deleteAll()
        }
    }

    fun insertOrUpdate(obj: Balance) {
        realm.executeTransaction { realm1 ->
            realm1.insertOrUpdate(obj)
        }
    }

    fun deleteBalance() {
        realm.executeTransaction {
            it.delete(Balance::class.java)
        }
    }

    fun queryBalance(): RealmResults<Balance> {
        return realm.where(Balance::class.java).findAllAsync()
    }

}