package com.uxi.bambupay.repository

import com.uxi.bambupay.api.UbpService
import com.uxi.bambupay.model.ubp.Banks
import com.uxi.bambupay.model.ubp.PartnerToken
import com.uxi.bambupay.model.ubp.RequestTransfer
import com.uxi.bambupay.model.ubp.SingleTransfer
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

    fun loadPartnerCustomerToken(grantType: String, clientId: String, code: String, redirectUri: String) : Flowable<PartnerToken> {
        return webService.partnerCustomerAccessToken(grantType, clientId, code, redirectUri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadSingleTransfer(clientId: String, clientSecret: String, partnerId: String, token: String, requestParams: RequestTransfer) : Flowable<SingleTransfer> {
        val authorization = "Bearer $token"
        return webService.instapayPartnerUbpSingleTransfer(clientId, clientSecret, partnerId, authorization, requestParams)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}