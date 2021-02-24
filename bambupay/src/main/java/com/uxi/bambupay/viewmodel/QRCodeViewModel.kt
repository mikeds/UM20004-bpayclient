package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.model.ResultWithMessage
import com.uxi.bambupay.model.ScanQr
import com.uxi.bambupay.repository.QrCodeRepository
import com.uxi.bambupay.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
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

    private val _validationSuccess = MutableLiveData<Boolean>()
    val validationSuccess: LiveData<Boolean> = _validationSuccess

    private val _successMessage = MutableLiveData<String>()
    private val _scanPayQr = MutableLiveData<ScanQr>()
    val scanPayQrDataWithMessage: MediatorLiveData<Pair<String?, ScanQr?>> = MediatorLiveData<Pair<String?, ScanQr?>>()
        .apply {
            addSource(_successMessage) { message ->
                this.value = this.value?.copy(first = message) ?: Pair(message, null)
            }
            addSource(_scanPayQr) {
                this.value = this.value?.copy(second = it) ?: Pair(null, it)
            }
        }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun subscriptionQuickPay(merchantId: String?, amount: String?) {

        Timber.tag("DEBUG").e("merchantId:: $merchantId")
        Timber.tag("DEBUG").e("amount:: $amount")
        loading.value = true
        repository.loadQuickPayQrMerchant(merchantId!!)
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
                    quickPaySuccessMsg.value = "Thank you for using QuickPayQR!"//it.successMessage
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
            .addTo(disposable)

    }

    fun subscribeCreatePayQr(amount: String?) {
        if (amount.isNullOrEmpty()) {
            isAmountEmpty.value = true
            return
        }

        val request = Request.Builder()
            .setAmount(amount).build()

        repository.loadCreatePayQr(request)
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
            .addTo(disposable)
    }

    fun validation(refIdNumber: String) {
        if (refIdNumber.isNullOrEmpty()) {
            isTransactionNumberEmpty.value = true
            return
        }

        _validationSuccess.postValue(true)
    }

    private fun <T : Any> resultState(t: ResultWithMessage<T>) {
        when (t) {
            is ResultWithMessage.Success -> {
                when (t.value) {
                    is ScanQr -> {
                        val scanQr = t.value as ScanQr
                        _scanPayQr.postValue(scanQr)
                        _successMessage.postValue("Thank you for using ScanPayQR!")
                    }
                }
            }
            is ResultWithMessage.Error -> {
                if (t.refresh) {
                    utils.saveUserTokenPack("", true)
                    isSuccess.value = false
                } else {
                    errorMessage.value = t.message
                }
                loading.value = false
                isSuccess.value = false
                _isLoading.value = false
            }
        }
    }

    fun subscribeScanPayQr(refIdNumber: String?) {
        if (refIdNumber.isNullOrEmpty()) {
            isTransactionNumberEmpty.value = true
            return
        }

        val requestBuilder = Request.Builder()
            .setSenderRefId(refIdNumber).build()

        repository.loadAcceptPayQr(requestBuilder)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                resultState(it)
            }, Timber::e)
            .addTo(disposable)

    }

}