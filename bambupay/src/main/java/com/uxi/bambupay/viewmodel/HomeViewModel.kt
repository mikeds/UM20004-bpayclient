package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.repository.HomeRepository
import com.uxi.bambupay.utils.Utils
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject
constructor(private val repository: HomeRepository, private val utils: Utils) : BaseViewModel() {

    private var subscriptionUIBalance: Disposable? = null
    val textBalance = MutableLiveData<String>()

    fun rxUIBalance() {
        if (subscriptionUIBalance == null || subscriptionUIBalance!!.isDisposed) {
            subscriptionUIBalance = repository.filterBalance()
                .subscribe({
                    it?.let { results ->
                        if (results.size > 0 && results[0] != null) {
                            val balance = results[0]
                            val balFormat = utils.currencyFormat(balance?.balance!!)
                            textBalance.value = balFormat
                        }
                    }
                }, {
                    Timber.e(it)
                })
        }
    }

    fun subscribeUserBalance() {
        disposable?.add(repository.loadBalance()
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.response != null) {
                    it.response?.let { userBalance ->
                        repository.loadDeleteBalance()
                        repository.saveBalance(userBalance)
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