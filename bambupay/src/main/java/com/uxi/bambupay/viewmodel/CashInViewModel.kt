package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.model.PairData
import com.uxi.bambupay.model.ResultWithMessage
import com.uxi.bambupay.model.paynamics.Paynamics
import com.uxi.bambupay.repository.CashInRepository
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.utils.Utils
import io.reactivex.rxkotlin.addTo
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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _paynamicsData = MutableLiveData<Paynamics>()
    private val _successMessage = MutableLiveData<String>()

    val paynamicsDataWithMessage: MediatorLiveData<Pair<String?, Paynamics?>> = MediatorLiveData<Pair<String?, Paynamics?>>()
        .apply {
            addSource(_successMessage) { message ->
               this.value = this.value?.copy(first = message) ?: Pair(message, null)
            }
            addSource(_paynamicsData) {
                this.value = this.value?.copy(second = it) ?: Pair(null, it)
            }
        }

    fun subscribeCashIn(amount: String?/*, recipient: String?*/) {
        if (amount.isNullOrEmpty()) {
            isAmountEmpty.value = true
            return
        }

        /*if (recipient.isNullOrEmpty()) {
            isRecipientEmpty.value = true
            return
        }*/

        val requestBuilder = Request.Builder()
            .setType(Constants.TYPE_OTC)
            .setAmount(amount).build()

        disposable?.add(repository.loadCashIn(requestBuilder)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.response != null) {
                    it.response?.let { cashIn ->
                        Log.e("DEBUG", "CashIn :: ${cashIn.toString()}")
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

    fun subscribeCashInPaynamics(amount: String?, cardNum: String?, cardMonthYear: String?, cvv: String?, cardName: String?) {
        if (amount.isNullOrEmpty() || cardNum.isNullOrEmpty() || cardMonthYear.isNullOrEmpty() || cvv.isNullOrEmpty() || cardName.isNullOrEmpty()) {
            errorMessage.value = "All fields are required!"
            return
        }

        if (cardMonthYear.isNullOrEmpty() || !cardMonthYear.contains("/")) {
            errorMessage.value = "Invalid card expiration"
            Timber.tag("DEBUG").e("invalid card expiration")
            return
        }

        val tempCardMonYr = cardMonthYear.split("/")
        val month = tempCardMonYr[0]
        val year = tempCardMonYr[1]

        if (month.isEmpty() || year.isEmpty()) {
            Timber.tag("DEBUG").e("month or year empty")
            return
        }

        repository.loadCashInPaynamics(amount, cardName, cardNum, month, year, cvv)
            .doOnSubscribe { _isLoading.value = true }
            .doOnComplete { _isLoading.value = false }
            .subscribe({
                resultState(it)
            }, Timber::e)
            .addTo(disposable)
    }

    private fun <T : Any> resultState(t: ResultWithMessage<T>) {
        when (t) {
            is ResultWithMessage.Success -> {
                when (t.value) {
                    is Paynamics -> {
                        val paynamics = t.value as Paynamics
                        _paynamicsData.postValue(paynamics)
                        _successMessage.postValue(t.message)
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
            }
        }
    }

}