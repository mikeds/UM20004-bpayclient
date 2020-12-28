package com.uxi.bambupay.repository

import com.uxi.bambupay.api.GenericApiResponse
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.model.CashOut
import com.uxi.bambupay.model.ResultWithMessage
import com.uxi.bambupay.model.paynamics.Paynamics
import com.uxi.bambupay.model.ubp.Bank
import com.uxi.bambupay.model.ubp.UbpCashOut
import com.uxi.bambupay.model.ubp.UbpToken
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/15/20.
 * hunterxer31@gmail.com
 */
class CashOutRepository @Inject
constructor(private val webService: WebService): BaseRepository() {

    fun loadCashOut(request: Request) : Flowable<GenericApiResponse<CashOut>> {
        return webService.cashOut(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadUbpBanks(): Flowable<ResultWithMessage<MutableList<Bank>>> {
        return webService.getUbpBanks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: MutableList<Bank>? = res.response) {
                    null -> ResultWithMessage.Error(false, "")
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
            .onErrorReturn { errorHandler(it) }
    }

    /*fun loadUbpToken(bankCode: Long): Flowable<ResultWithMessage<UbpToken>> {
        return webService.ubpToken(bankCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: UbpToken? = res.response) {
                    null -> ResultWithMessage.Error(false, "")
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
            .onErrorReturn { errorHandler(it) }
    }*/

    fun loadUbpToken(bankCode: Long): Flowable<UbpToken> {
        return webService.ubpToken(bankCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                res.response
            }
    }

    fun loadUbpCashOut(
        bankCode: Long,
        accountNo: String,
        amount: String,
        firstName: String,
        lastName: String,
        mobileNo: String,
        accessToken: String
    ): Flowable<ResultWithMessage<UbpCashOut>> {
        val requestBody = hashMapOf(
            "type" to "ubp",
            "bank_code" to bankCode.toString(),
            "account_no" to accountNo,
            "amount" to amount,
            "first_name" to firstName,
            "middle_name" to "",
            "last_name" to lastName,
            "mobile_no" to mobileNo
        )
        return webService.ubpCashOut(requestBody, accessToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: UbpCashOut? = res.response) {
                    null -> ResultWithMessage.Error(false, "")
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
            .onErrorReturn { errorHandler(it) }
    }

}