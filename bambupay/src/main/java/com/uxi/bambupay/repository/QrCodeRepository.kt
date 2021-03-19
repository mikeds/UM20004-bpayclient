package com.uxi.bambupay.repository

import com.uxi.bambupay.api.GenericApiResponse
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.api.WebService
import com.uxi.bambupay.model.MerchantInfo
import com.uxi.bambupay.model.ResultWithMessage
import com.uxi.bambupay.model.ScanQr
import com.uxi.bambupay.model.lookup.TxDetails
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 10/5/20.
 * hunterxer31@gmail.com
 */
@Singleton
class QrCodeRepository @Inject constructor(private val webService: WebService): BaseRepository() {

    fun loadCreatePayQr(request: Request) : Flowable<GenericApiResponse<ScanQr>> {
        return webService.createPayQr(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadAcceptPayQr(refIdNumber: String?) : Flowable<ResultWithMessage<ScanQr>> {
        val requestBuilder = Request.Builder()
            .setSenderRefId(refIdNumber).build()
        return webService.acceptPayQr(requestBuilder)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: ScanQr? = res.response) {
                    null -> ResultWithMessage.Error(false, "")
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
            .onErrorReturn { errorHandler(it) }
    }

    fun loadQuickPayQrMerchant(merchantId: String) : Flowable<GenericApiResponse<MerchantInfo>> {
        return webService.quickPayQRMerchant(merchantId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadQuickPayQrAccept(request: Request) : Flowable<GenericApiResponse<ScanQr>> {
        return webService.quickPayQRAccept(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadTxDetails(refIdNumber: String?): Flowable<ResultWithMessage<TxDetails>> {
        return webService.getTxDetails(refIdNumber!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: TxDetails? = res.response) {
                    null -> ResultWithMessage.Error(false, "")
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
            .onErrorReturn { errorHandler(it) }
    }

}