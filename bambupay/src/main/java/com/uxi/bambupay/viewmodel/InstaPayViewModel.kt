package com.uxi.bambupay.viewmodel

import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.model.ubp.Bank
import com.uxi.bambupay.repository.InstaPayRepository
import com.uxi.bambupay.utils.Utils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 8/15/20.
 * hunterxer31@gmail.com
 */
class InstaPayViewModel @Inject
constructor(private val repository: InstaPayRepository, private val utils: Utils) : BaseViewModel() {

    val banksData = MutableLiveData<ArrayList<Bank>>()

    fun subscribeInstaPayBanks() {

        disposable?.add(repository.loadBanks(utils.ubpClientId!!, utils.ubpClientSecret!!, utils.ubpPartnerId!!)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({

                Timber.tag("DEBUG").e("BANKS:: ${it.records?.size}")
                banksData.value = it.records

            }, {
                Timber.e(it)
            })
        )

    }

}