package com.uxi.bambupay.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uxi.bambupay.model.SingleEvent

/**
 * Created by Eraño Payawal on 1/13/21.
 * hunterxer31@gmail.com
 */
class SharedOtpViewModel : ViewModel() {

    private val _onViewCashOut = MutableLiveData<SingleEvent<Boolean>>()
    val onViewCashOut: LiveData<SingleEvent<Boolean>> = _onViewCashOut

    fun doViewOnViewCashOut() {
        _onViewCashOut.postValue(SingleEvent(true))
    }

}