package com.uxi.bambupay.repository

import com.uxi.bambupay.api.GenericApiResponse
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.model.User
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/20/20.
 * hunterxer31@gmail.com
 */
class VerifyRepository @Inject
constructor(
    private val webService: WebService
) {

    fun loadResendVerification(request: Request) : Flowable<GenericApiResponse<User>> {
        return webService.resendVerificationCode(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadVerifyCode(request: Request) : Flowable<GenericApiResponse<User>> {
        return webService.verificationCode(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}