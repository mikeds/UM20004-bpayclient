package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.model.RecentTransaction
import com.uxi.bambupay.model.ResultWithMessage
import com.uxi.bambupay.model.SendMoney
import com.uxi.bambupay.model.Transaction
import com.uxi.bambupay.repository.TransactionRepository
import com.uxi.bambupay.utils.Utils
import io.reactivex.rxkotlin.addTo
import io.realm.OrderedRealmCollection
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/13/20.
 * hunterxer31@gmail.com
 */
class TransactionViewModel @Inject
constructor(private val repository: TransactionRepository, private val utils: Utils) : BaseViewModel() {

    val isAmountEmpty = MutableLiveData<Boolean>()
    val isRecipientEmpty = MutableLiveData<Boolean>()
    val sendMoneySuccessMsg = MutableLiveData<String>()
    val transactionData = MutableLiveData<Transaction>()
    val transactionRecentData = MutableLiveData<RecentTransaction>()

    var isPullToRefresh: Boolean = false
    private var pageOffset: Int = 0

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _successMessage = MutableLiveData<String>()
    private val _sendMoney = MutableLiveData<SendMoney>()
    val sendMoneyDataWithMessage: MediatorLiveData<Pair<String?, SendMoney?>> = MediatorLiveData<Pair<String?, SendMoney?>>()
        .apply {
            addSource(_successMessage) { message ->
                this.value = this.value?.copy(first = message) ?: Pair(message, null)
            }
            addSource(_sendMoney) {
                this.value = this.value?.copy(second = it) ?: Pair(null, it)
            }
        }

    private val _validationSuccess = MutableLiveData<Boolean>()
    val validationSuccess: LiveData<Boolean> = _validationSuccess

    fun subscribeTransactions() {

        if (isPullToRefresh) {
            loading.value = true
            pageOffset = 0
            isPullToRefresh = false
        }

        disposable?.add(repository.loadTransactions()
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.response != null) {
                    it.response?.let { transactions ->
                        transactions.lastId?.let { it1 -> utils.saveLastTransactionId(it1) }
                        transactions.data?.let { it1 -> repository.saveTransactions(it1) }
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

    fun subscribeTransactionsMore() {

        val transaction = repository.loadLastTransaction()
        val lastTransactionId = utils.userLastTransactionId

        if (transaction != null) {
            val transactionId = transaction.id

            if (lastTransactionId == transactionId) {
                loading.value = false
            } else {
                disposable?.add(repository.loadTransactionsMore(transactionId!!)
                    .doAfterTerminate { loading.value = false }
                    .subscribe({
                        if (it.response != null) {
                            it.response?.let { transactions ->
                                transactions.lastId?.let { it1 -> utils.saveLastTransactionId(it1) }
                                transactions.data?.let { it1 -> repository.saveTransactions(it1) }
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

        } else {
            loading.value = false
        }

    }

    fun subscribeRecentTransactions() {
        disposable?.add(repository.loadRecentTransactions()
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.response != null) {
                    it.response?.let { transactions ->
                        repository.deleteRecent()

                        Timber.tag("DEBUG").e("lastId ${transactions.lastId}")
                        transactions.data?.let { it1 -> repository.saveRecentTransactions(it1) }

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

    fun filterTransactions() : OrderedRealmCollection<Transaction> {
        return repository.filter()
    }

    fun filterRecentTransactions() : OrderedRealmCollection<RecentTransaction> {
        return repository.filterRecent()
    }

    fun validation(amount: String?, recipient: String?) {
        if (amount.isNullOrEmpty()) {
            isAmountEmpty.value = true
            return
        }

        if (recipient.isNullOrEmpty()) {
            isRecipientEmpty.value = true
            return
        }

        _validationSuccess.postValue(true)
    }

    fun subscribeSendMoney(amount: String?, recipient: String?, message: String?) {
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
            .setUsername(recipient)

        if (!message.isNullOrEmpty()) {
            requestBuilder.setMessage(message)
        }

        repository.loadSendMoney(requestBuilder.build())
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                resultState(it)
            }, Timber::e)
            .addTo(disposable)
    }

    private fun <T : Any> resultState(t: ResultWithMessage<T>) {
        when (t) {
            is ResultWithMessage.Success -> {
                when (t.value) {
                    is SendMoney -> {
                        val sendMoney = t.value as SendMoney
                        _sendMoney.postValue(sendMoney)
                        _successMessage.postValue("Thank you for using Send Money!")
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

    fun subscribeTransactionId(transactionId: String?, historyType: String?) {
        transactionId?.let {
            if (historyType.isNullOrEmpty()) {
                val transaction = repository.loadTransaction(it)
                transactionData.value = transaction
            } else if (!historyType.isNullOrEmpty() && historyType == "RECENT") {
                val recentTransaction = repository.loadRecentTransaction(it)
                transactionRecentData.value = recentTransaction
            }
        }
    }

    fun formatAmount(amount: String) : String? {
        return utils.currencyFormat(amount)
    }

}