package com.uxi.bambupay.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.model.SingleEvent
import com.uxi.bambupay.repository.FeeRepository
import com.uxi.bambupay.utils.Utils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 11/4/20.
 * hunterxer31@gmail.com
 */
class FeeViewModel @Inject
constructor(private val repository: FeeRepository, private val utils: Utils) : BaseViewModel() {

    private val _fees = MutableLiveData<SingleEvent<String>>()
    val fees: LiveData<SingleEvent<String>> = _fees

    fun subscribeFee(amount: String?, txTypeId: String?) {
        if (amount.isNullOrEmpty() || txTypeId.isNullOrEmpty()) return

        disposable?.add(
            repository.loadFee(txTypeId, amount)
                .subscribe({ res ->
                    res.response?.let {
                        it.fee?.let { fee ->
                            _fees.postValue(SingleEvent(fee))
                        }
                    }
                }, Timber::e)
        )
    }

}