package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.repository.CashOutRepository
import com.uxi.bambupay.utils.Utils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/15/20.
 * hunterxer31@gmail.com
 */
class CashOutViewModel @Inject
constructor(private val repository: CashOutRepository, private val utils: Utils) : BaseViewModel() {

    val isAmountEmpty = MutableLiveData<Boolean>()
    val isRecipientEmpty = MutableLiveData<Boolean>()
    val isCashOutSuccess = MutableLiveData<Boolean>()

    fun subscribeCashOut(amount: String?, recipient: String?) {
        if (amount.isNullOrEmpty()) {
            isAmountEmpty.value = true
            return
        }

        if (recipient.isNullOrEmpty()) {
            isRecipientEmpty.value = true
            return
        }

        val requestBuilder = Request.Builder()
            .setMerchant(recipient)
            .setAmount(amount).build()

        disposable?.add(repository.loadCashOut(requestBuilder)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.response != null) {
                    it.response?.let { cashOut ->
                        Log.e("DEBUG", "CashOut :: ${cashOut.toString()}")
                        isCashOutSuccess.value = true
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

}