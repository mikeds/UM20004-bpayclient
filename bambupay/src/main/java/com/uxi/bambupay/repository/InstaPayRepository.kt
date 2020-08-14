package com.uxi.bambupay.repository

import com.uxi.bambupay.api.UbpService
import com.uxi.bambupay.model.ubp.Banks
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 8/15/20.
 * hunterxer31@gmail.com
 */
class InstaPayRepository @Inject
constructor(private val webService: UbpService) {

    fun loadBanks(clientId: String, clientSecret: String, partnerId: String) : Flowable<Banks> {
        return webService.instapayPartners(clientId, clientSecret, partnerId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}