package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.model.Transaction
import com.uxi.bambupay.repository.TransactionRepository
import com.uxi.bambupay.utils.Utils
import io.realm.OrderedRealmCollection
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/13/20.
 * hunterxer31@gmail.com
 */
class TransactionViewModel @Inject
constructor(private val repository: TransactionRepository, private val utils: Utils) : BaseViewModel() {

    val isSuccess = MutableLiveData<Boolean>()

    fun subscribeTransactions() {
        disposable?.add(repository.loadTransactions()
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.value != null) {
                    it.value?.let { transactions ->
                        transactions.history?.let { it1 -> repository.saveTransactions(it1) }

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

    fun filterTransactions() : OrderedRealmCollection<Transaction> {
        return repository.filter()
    }

}