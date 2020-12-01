package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.model.ScanQr
import com.uxi.bambupay.repository.QrCodeRepository
import com.uxi.bambupay.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 10/5/20.
 * hunterxer31@gmail.com
 */
class QRCodeViewModel @Inject
constructor(private val repository: QrCodeRepository, private val utils: Utils) : BaseViewModel() {

    val quickPaySuccessMsg = MutableLiveData<String>()
    val quickPayData = MutableLiveData<ScanQr>()
    val createPayQrData = MutableLiveData<ScanQr>()
    val isAmountEmpty = MutableLiveData<Boolean>()

    val isTransactionNumberEmpty = MutableLiveData<Boolean>()
    val successMessage = MutableLiveData<String>()

    fun subscriptionQuickPay(merchantId: String?, amount: String?) {

        Timber.tag("DEBUG").e("merchantId:: $merchantId")
        Timber.tag("DEBUG").e("amount:: $amount")
        loading.value = true
        disposable?.add(repository.loadQuickPayQrMerchant(merchantId!!)
            .flatMap { t ->
                val merchantAccountNumber = t.response?.accountNumber
                val request = Request.Builder()
                    .setAccountNum(merchantAccountNumber)
                    .setAmount(amount!!).build()
                repository.loadQuickPayQrAccept(request)
            }
            .doAfterTerminate { loading.value = false }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                if (it.response != null) {
                    quickPaySuccessMsg.value = it.successMessage
                    quickPayData.value = it.response

                } else {
                    it.errorMessage?.let { error ->
                        errorMessage.value = error
                    }
                }

            }, {
                Timber.e(it)
                loading.value = false
                if (refreshToken(it)) {
                    Timber.tag("DEBUG").e("Error RefreshToken")
                    utils.saveUserTokenPack("", true)
                    isSuccess.value = false
                }
            })

        )

    }

    fun subscribeCreatePayQr(amount: String?) {
        if (amount.isNullOrEmpty()) {
            isAmountEmpty.value = true
            return
        }

        val request = Request.Builder()
            .setAmount(amount).build()

        disposable?.add(repository.loadCreatePayQr(request)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.response != null) {
                    it.response?.let { it1 ->
                        createPayQrData.value = it1
                    }
                } else {
                    it.errorMessage?.let { error ->
                        errorMessage.value = error
                        Log.e("DEBUG", "error message:: $error")
                    }
                }

            }, {
                Timber.e(it)
                if (refreshToken(it)) {
                    Log.e("DEBUG", "error refreshToken")
                    utils.saveUserTokenPack("", true)
                    isSuccess.value = false
                }
            })
        )
    }

    fun subscribeScanPayQr(refIdNumber: String) {
        if (refIdNumber.isNullOrEmpty()) {
            isTransactionNumberEmpty.value = true
            return
        }

        val requestBuilder = Request.Builder()
            .setSenderRefId(refIdNumber).build()

        disposable?.add(repository.loadAcceptPayQr(requestBuilder)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.response != null) {
                    quickPaySuccessMsg.value = it.successMessage
                    quickPayData.value = it.response
                } else {
                    it.errorMessage?.let { error ->
                        errorMessage.value = error
                        Log.e("DEBUG", "error message:: $error")
                    }
                    it.successMessage?.let { message ->
                        successMessage.value = message
                    }
                }

            }, {
                Timber.e(it)
                if (refreshToken(it)) {
                    Log.e("DEBUG", "error refreshToken")
                    utils.saveUserTokenPack("", true)
                    isSuccess.value = false
                }
            })
        )

    }

}