package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.model.RecentTransaction
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
    val isAmountEmpty = MutableLiveData<Boolean>()
    val isRecipientEmpty = MutableLiveData<Boolean>()
    val isSendMoneySuccess = MutableLiveData<Boolean>()
    val transactionData = MutableLiveData<Transaction>()

    var isPullToRefresh: Boolean = false
    private var pageOffset: Int = 0

    fun subscribeTransactions() {

        if (isPullToRefresh) {
            loading.value = true
            pageOffset = 0
            isPullToRefresh = false
        }
        
        disposable?.add(repository.loadTransactions()
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.value != null) {
                    it.value?.let { transactions ->
                        transactions.lastTransactionId?.let { it1 -> utils.saveLastTransactionId(it1) }
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

    fun subscribeTransactionsMore() {

        val transaction = repository.loadLastTransaction()
        val lastTransactionId = utils.userLastTransactionId

        if (transaction != null) {
            val transactionId = transaction.transactionId

            if (lastTransactionId == transactionId) {
                loading.value = false
            } else {
                disposable?.add(repository.loadTransactionsMore(transactionId!!)
                    .doAfterTerminate { loading.value = false }
                    .subscribe({
                        if (it.value != null) {
                            it.value?.let { transactions ->
                                transactions.lastTransactionId?.let { it1 -> utils.saveLastTransactionId(it1) }
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

        } else {
            loading.value = false
        }

    }

    fun subscribeRecentTransactions() {
        disposable?.add(repository.loadRecentTransactions()
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.value != null) {
                    it.value?.let { transactions ->
                        repository.deleteRecent()
                        transactions.history?.let { it1 -> repository.saveRecentTransactions(it1) }

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

    fun filterRecentTransactions() : OrderedRealmCollection<RecentTransaction> {
        return repository.filterRecent()
    }

    fun subscribeSendMoney(amount: String?, recipient: String?) {
        if (amount.isNullOrEmpty()) {
            isAmountEmpty.value = true
            return
        }

        if (recipient.isNullOrEmpty()) {
            isRecipientEmpty.value = true
            return
        }

        val requestBuilder = Request.Builder()
            .setAmount(amount)
            .setSendTo(recipient).build()

        disposable?.add(repository.loadSendMoney(requestBuilder)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.value != null) {
                    it.value?.let { pay ->
                        isSendMoneySuccess.value = true
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

    fun subscribeTransactionId(transactionId: Long?) {
        transactionId?.let {
            if (it > 0) {
                val transaction = repository.loadTransaction(it)
                transactionData.value = transaction
            }
        }
    }

}