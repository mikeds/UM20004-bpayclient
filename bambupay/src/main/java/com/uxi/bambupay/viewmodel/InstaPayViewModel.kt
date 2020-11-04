package com.uxi.bambupay.viewmodel

import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.model.ubp.Bank
import com.uxi.bambupay.model.ubp.RequestTransfer
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
    val accessToken = MutableLiveData<String>()
    val successTransfer = MutableLiveData<Boolean>()

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

    fun subscribeUbpSingleTransfer(accountName: String?, accountNo: String, message: String?, amount: String, accessToken: String) {

        val isoTimeStamp = utils.getIsoTimeStamp()

        Timber.tag("DEBUG").e("isoTimeStamp $isoTimeStamp")

        var request = RequestTransfer.Builder()
            .senderRefId("123456")
            .tranRequestDate(isoTimeStamp)
            .accountNo(accountNo)
            .amount("PHP", amount)
            .remarks("Transfer remarks")
            .particulars("Transfer particulars")
            .info(1, "Recipient", accountName!!)
            .info(2, "Message", message!!).build()


        disposable?.add(repository.loadSingleTransfer(utils.ubpClientId!!, utils.ubpClientSecret!!, utils.ubpPartnerId!!, accessToken, request)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({

                successTransfer.value = true

            }, {
                Timber.e(it)
            })
        )
    }

    fun subscribePartnerToken() {
        disposable?.add(repository.loadPartnerCustomerToken("authorization_code",
            utils.ubpClientId!!, utils.ubpLoginCode!!, "https://dev-api.resolveitthrough.us/callback/ubp/code")
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({

                accessToken.value = it.accessToken

            }, {
                Timber.e(it)
            })
        )
    }

    fun saveCode(code: String?) {
        code?.let {
            utils.saveUbnLoginCode(it)
        }
    }

}