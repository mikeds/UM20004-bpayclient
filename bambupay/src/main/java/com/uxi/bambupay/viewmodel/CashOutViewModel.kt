package com.uxi.bambupay.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.model.ResultWithMessage
import com.uxi.bambupay.model.ubp.Bank
import com.uxi.bambupay.model.ubp.UbpCashOut
import com.uxi.bambupay.model.ubp.UbpToken
import com.uxi.bambupay.repository.CashOutRepository
import com.uxi.bambupay.repository.MainRepository
import com.uxi.bambupay.utils.Utils
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/15/20.
 * hunterxer31@gmail.com
 */
class CashOutViewModel @Inject
constructor(private val repository: CashOutRepository,
            private val repositoryMain: MainRepository,
            private val utils: Utils) : BaseViewModel() {

    val isAmountEmpty = MutableLiveData<Boolean>()
    val isRecipientEmpty = MutableLiveData<Boolean>()
    val isCashOutSuccess = MutableLiveData<Boolean>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _banksData = MutableLiveData<MutableList<Bank>>()
    val banksData: LiveData<MutableList<Bank>> = _banksData

    private val _ubpToken = MutableLiveData<String>()
    private val _successMessage = MutableLiveData<String>()
    private val _ubpCashOut = MutableLiveData<UbpCashOut>()
    private val _validationSuccess = MutableLiveData<Boolean>()
    val validationSuccess: LiveData<Boolean> = _validationSuccess

    val ubpCashOutDataWithMessage: MediatorLiveData<Pair<String?, UbpCashOut?>> = MediatorLiveData<Pair<String?, UbpCashOut?>>()
        .apply {
            addSource(_successMessage) { message ->
                this.value = this.value?.copy(first = message) ?: Pair(message, null)
            }
            addSource(_ubpCashOut) {
                this.value = this.value?.copy(second = it) ?: Pair(null, it)
            }
        }

    /*fun subscribeCashOut(amount: String?, recipient: String?) {
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

    }*/

    private fun <T : Any> resultState(t: ResultWithMessage<T>) {
        when (t) {
            is ResultWithMessage.Success -> {
                when (t.value) {
                    is UbpToken -> {
                        val ubpToken = t.value as UbpToken?
                        _ubpToken.postValue(ubpToken?.accessToken)
                    }
                    is MutableList<*> -> {
                        val banks = t.value as MutableList<Bank>
                        _banksData.postValue(banks)
                    }
                    is UbpCashOut -> {
                        val ubpCashOut = t.value as UbpCashOut
                        _ubpCashOut.postValue(ubpCashOut)
//                        _successMessage.postValue(t.message)
                        _successMessage.postValue("Thank you for using Cash Out!")
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

    fun subscribeGetBanks() {
        repository.loadUbpBanks()
            .doOnSubscribe { _isLoading.value = true }
            .doOnComplete { _isLoading.value = false }
            .subscribe({
                resultState(it)
            }, Timber::e)
            .addTo(disposable)
    }

    fun validation(amount: String?, accountNo: String?, bankCode: Long?) {
        if (bankCode == -0L) return

        if (amount.isNullOrEmpty() || accountNo.isNullOrEmpty()) {
            errorMessage.value = "All fields are required!"
            return
        }

        _validationSuccess.postValue(true)
    }

    fun subscribeCashOut(amount: String?, accountNo: String?, bankCode: Long?) {
        if (bankCode == -0L) return

        if (amount.isNullOrEmpty() || accountNo.isNullOrEmpty()) {
            errorMessage.value = "All fields are required!"
            return
        }

        val user = repositoryMain.loadCurrentUser() ?: return

        if (user.firstName.isNullOrEmpty() || user.lastName.isNullOrEmpty() || user.mobileNumber.isNullOrEmpty()) {
            return
        }

        _isLoading.value = true
        repository.loadUbpToken(bankCode!!)
            .flatMap { res ->
                Timber.tag("DEBUG").e("UBP TOKEN ${res?.accessToken}")
                repository.loadUbpCashOut(bankCode, accountNo, amount, user.firstName!!, user.lastName!!, user.mobileNumber!!, res?.accessToken!!)
            }
            .doOnComplete { _isLoading.value = false }
            .subscribeOn(Schedulers.io())
            .subscribe({
                resultState(it)
            }, Timber::e)
            .addTo(disposable)

    }

}