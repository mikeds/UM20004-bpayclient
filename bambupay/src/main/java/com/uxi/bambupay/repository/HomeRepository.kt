package com.uxi.bambupay.repository

import com.uxi.bambupay.api.GenericApiResponse
import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.db.TransactionDao
import com.uxi.bambupay.db.UserDao
import com.uxi.bambupay.model.Balance
import com.uxi.bambupay.model.Transaction
import com.uxi.bambupay.model.Transactions
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 7/13/20.
 * hunterxer31@gmail.com
 */
@Singleton
class HomeRepository @Inject constructor(private val userDao: UserDao,
                                         private val webService: WebService) {

    fun loadBalance() : Flowable<GenericApiResponse<Balance>> {
        return webService.balance()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadDeleteBalance() {
        userDao.deleteBalance()
    }

    fun saveBalance(balance: Balance) {
        userDao.insertOrUpdate(balance)
    }

    fun filterBalance(): Flowable<RealmResults<Balance>> {
        return userDao.queryBalance()
            .asFlowable()
            .observeOn(AndroidSchedulers.mainThread())
    }

}