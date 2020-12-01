package com.uxi.bambupay.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.model.SingleEvent
import com.uxi.bambupay.repository.MainRepository
import com.uxi.bambupay.repository.OtpRepository
import com.uxi.bambupay.utils.Utils
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 12/1/20.
 * hunterxer31@gmail.com
 */
class OtpViewModel @Inject
constructor(
    private val repository: OtpRepository,
    private val mainRepo: MainRepository,
    private val utils: Utils
) : BaseViewModel() {

    private val _redirectUrl = MutableLiveData<String>()
    val redirectUrl: LiveData<String> = _redirectUrl

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccessOtp = MutableLiveData<SingleEvent<Boolean>>()
    val isSuccessOtp: LiveData<SingleEvent<Boolean>> = _isSuccessOtp

    private val _errorMessage = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMessage

    fun subscribeRequestOtp() {
        val user = mainRepo.loadCurrentUser()

        user?.let { it1 ->
            val mobileNumber = it1.mobileNumber
            if (mobileNumber.isNullOrEmpty()) return

            var phoneNumber = ""
            if (mobileNumber.startsWith("63")) {
                phoneNumber = mobileNumber.removeRange(0, 2)
            }

            repository.loadRequestOTP(phoneNumber)
                .doOnSubscribe { _isLoading.value = true }
                .doAfterTerminate { _isLoading.value = false }
                .subscribe({ res->
                    res?.redirectUrl?.let {
                        Timber.tag("DEBUG").e("redirectUrl=> $it")
                        _redirectUrl.postValue(it)
                    }
                }, Timber::e)
                .addTo(disposable)
        }
    }

    fun subscribeOtpCode(code: String?) {
        if (code.isNullOrEmpty()) return

        Timber.tag("DEBUG").e("GLobe Code::$code")
        repository.loadTokenOTP(code)
            .doOnSubscribe { _isLoading.value = true }
            .doAfterTerminate { _isLoading.value = false }
            .subscribe({ res ->
                res?.response?.let {
                    Timber.tag("DEBUG").e("token => ${it.accessToken}")
                    Timber.tag("DEBUG").e("subsNum => ${it.subscriberNumber}")
                    if (!it.accessToken.isNullOrEmpty()) {
                        subscribeRequestOtp()
                    }
                }
            }, Timber::e)
            .addTo(disposable)
    }

    fun subscribeSubmitOTP(otp: String?) {
        Timber.tag("DEBUG").e("OTP CODE:: $otp")
        if (otp.isNullOrEmpty()) return

        repository.loadSubmitOTP(otp)
            .doOnSubscribe { _isLoading.value = true }
            .doAfterTerminate { _isLoading.value = false }
            .subscribe({ it1 ->
                if (it1?.errorMessage.isNullOrEmpty()) {
                    Timber.tag("DEBUG").e("SUCCESS SENDING OTP")
                    _isSuccessOtp.postValue(SingleEvent(true))
                } else {
                    _errorMessage.postValue(it1?.errorMessage)
                }
            }, Timber::e)
            .addTo(disposable)
    }

}