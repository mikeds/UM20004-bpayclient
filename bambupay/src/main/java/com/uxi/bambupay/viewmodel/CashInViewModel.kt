package com.uxi.bambupay.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.model.CashIn
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
    private val _cashInData = MutableLiveData<CashIn>()

    val paynamicsDataWithMessage: MediatorLiveData<Pair<String?, Paynamics?>> = MediatorLiveData<Pair<String?, Paynamics?>>()
        .apply {
            addSource(_successMessage) { message ->
               this.value = this.value?.copy(first = message) ?: Pair(message, null)
            }
            addSource(_paynamicsData) {
                this.value = this.value?.copy(second = it) ?: Pair(null, it)
            }
        }

    val cashInDataWithMessage: MediatorLiveData<Pair<String?, CashIn?>> = MediatorLiveData<Pair<String?, CashIn?>>()
        .apply {
            addSource(_successMessage) { message ->
                this.value = this.value?.copy(first = message) ?: Pair(message, null)
            }
            addSource(_cashInData) {
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

        repository.loadCashIn(requestBuilder)
            .doOnSubscribe { _isLoading.value = true }
            .doOnComplete { _isLoading.value = false }
            .subscribe({
                resultState(it)
            }, Timber::e)
            .addTo(disposable)

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
                    is CashIn -> {
                        val cashIn = t.value as CashIn
                        _cashInData.postValue(cashIn)
//                        _successMessage.postValue(t.message)
                        _successMessage.postValue("Go to the nearest BambuPay merchant and show the Merchant the generated QR code.")
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

}