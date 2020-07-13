package com.uxi.bambupay.db

import com.uxi.bambupay.model.Transaction
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

/**
 * Created by Eraño Payawal on 7/13/20.
 * hunterxer31@gmail.com
 */
class TransactionDao(val realm: Realm) {

    fun insertOrUpdate(obj: List<Transaction>) {
        realm.executeTransaction { realm1 ->
            realm1.insertOrUpdate(obj)
        }
    }

    fun query(): RealmResults<Transaction> {
        return realm.where(Transaction::class.java)
            .limit(10)
            .findAllAsync().sort("date", Sort.DESCENDING)
    }

}