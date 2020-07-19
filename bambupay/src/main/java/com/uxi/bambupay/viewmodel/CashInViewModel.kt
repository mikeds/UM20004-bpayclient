package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.repository.CashInRepository
import com.uxi.bambupay.utils.Utils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/15/20.
 * hunterxer31@gmail.com
 */
class CashInViewModel @Inject
constructor(private val repository: CashInRepository, private val utils: Utils) : BaseViewModel() {

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
                if (it.value != null) {
                    it.value?.let { cashIn ->
                        Log.e("DEBUG", "CashIn :: ${cashIn.toString()}")
                        isCashOutSuccess.value = true
                    }
                } else {
                    it.message?.let { error ->
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