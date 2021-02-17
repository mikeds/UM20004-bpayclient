package com.uxi.bambupay.repository

import com.uxi.bambupay.api.GenericApiResponse
import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.model.CashIn
import com.uxi.bambupay.model.ResultWithMessage
import com.uxi.bambupay.model.otp.OtpRes
import com.uxi.bambupay.model.otp.OtpResponse
import com.uxi.bambupay.model.otp.OtpTokenResponse
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 12/1/20.
 * hunterxer31@gmail.com
 */
@Singleton
class OtpRepository @Inject constructor(private val webService: WebService): BaseRepository() {

    fun loadRequestOTP(mobileNumber: String, module: String?): Flowable<ResultWithMessage<OtpResponse>> {
        val map = HashMap<String, String>()
        map["mobile_no"] = mobileNumber

        if (!module.isNullOrEmpty()) {
            map["module"] = module
        }
        return webService.requestOTP(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                if (res.error) {
                    if (res.redirectUrl.isNullOrEmpty()) {
                        ResultWithMessage.Error(false, res.errorDescription)
                    } else {
                        ResultWithMessage.Success(res, res.message)
                    }
                } else {
                    ResultWithMessage.Success(res, res.message)
                }

                /*when (val obj: OtpRes? = res.response) {
                    null -> ResultWithMessage.ErrorResult(false, res)
                    else -> ResultWithMessage.Success(res, res.message)
                }*/
            }
            .onErrorReturn { errorHandler(it) }
    }

    fun loadTokenOTP(code: String): Flowable<GenericApiResponse<OtpTokenResponse>> {
        return webService.tokenOTP(code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadSubmitOTP(otp: String, mobileNumber: String): Flowable<GenericApiResponse<Void>> {
        val map = HashMap<String, String>()
        map["otp"] = otp
        map["mobile_no"] = mobileNumber
        return webService.submitOTP(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}