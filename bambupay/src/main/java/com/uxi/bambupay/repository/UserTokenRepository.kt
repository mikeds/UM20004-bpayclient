package com.uxi.bambupay.repository

import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.model.TokenResponse
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 7/12/20.
 * hunterxer31@gmail.com
 */
@Singleton
class UserTokenRepository @Inject constructor(private val webService: WebService) {

    fun loadToken() : Flowable<TokenResponse> {
        val map = HashMap<String, String>()
        map["grant_type"] = "client_credentials"
        return webService.getToken(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}