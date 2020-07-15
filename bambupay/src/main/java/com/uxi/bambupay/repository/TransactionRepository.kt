package com.uxi.bambupay.repository

import com.uxi.bambupay.api.GenericApiResponse
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.db.TransactionDao
import com.uxi.bambupay.model.*
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/13/20.
 * hunterxer31@gmail.com
 */
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val webService: WebService) {

    fun loadTransactions(page: Int): Flowable<GenericApiResponse<Transactions>> {
        return webService.history(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadRecentTransactions(): Flowable<GenericApiResponse<RecentTransactions>> {
        return webService.recentTransactions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveTransactions(transactions: List<Transaction>) {
        transactionDao.insertOrUpdate(transactions)
    }

    fun filter(): RealmResults<Transaction> {
        return transactionDao.query()
    }

    fun saveRecentTransactions(transactions: List<RecentTransaction>) {
        transactionDao.insertOrUpdateRecent(transactions)
    }

    fun filterRecent(): RealmResults<RecentTransaction> {
        return transactionDao.queryRecent()
    }

    fun deleteRecent() {
        transactionDao.deleteRecent()
    }

    fun loadSendMoney(request: Request) : Flowable<GenericApiResponse<Pay>>{
        return webService.sendTo(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadTransaction(transactionId: Long) : Transaction? {
        return transactionDao.getTransaction(transactionId)
    }

}