package com.uxi.bambupay.repository

import com.uxi.bambupay.api.GenericApiResponse
import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.model.FeeResponse
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 11/4/20.
 * hunterxer31@gmail.com
 */
@Singleton
class FeeRepository @Inject constructor(private val webService: WebService) {

    fun loadFee(txTypeId: String, amount: String) : Flowable<GenericApiResponse<FeeResponse>> {
        return webService.transactionFee(txTypeId, amount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}