package com.uxi.bambupay.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.model.ResultWithMessage
import com.uxi.bambupay.model.SingleEvent
import com.uxi.bambupay.model.otp.OtpResponse
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

    private var mobileNum: String? = null
    private var module: String? = null//"login" // default
    private var userMobileNum: String? = null

    fun subscribeRequestOtp(mobileNum: String? = null, module: String? = null) {
        this.mobileNum = mobileNum

        Timber.tag("DEBUG").e("before module:: $module")
        if (!module.isNullOrEmpty()) {
           this.module = module
        }
        Timber.tag("DEBUG").e("after module:: ${this.module}")
        val user = mainRepo.loadCurrentUser()

        val tempNum = this.mobileNum ?: user?.mobileNumber
        Timber.tag("DEBUG").e("mobileNumber:: $tempNum")

        if (tempNum.isNullOrEmpty()) return

        val phoneNumber = when {
            tempNum.startsWith("63") -> {
                tempNum.removeRange(0, 2)
            }
            tempNum.startsWith("09") -> {
                tempNum.removeRange(0, 1)
            }
            else -> { tempNum }
        }

        Timber.tag("DEBUG").e("PASSED! phoneNumber => $phoneNumber")

        userMobileNum = phoneNumber

        repository.loadRequestOTP(phoneNumber, this.module)
            .doOnSubscribe { _isLoading.value = true }
            .doAfterTerminate { _isLoading.value = false }
            .subscribe({ res->
                /*res?.redirectUrl?.let {
                    Timber.tag("DEBUG").e("redirectUrl=> $it")
                    _redirectUrl.postValue(it)
                }*/

                when (res) {
                    is ResultWithMessage.Success -> {
                        val otpResponse = res.value as OtpResponse
                        Timber.tag("DEBUG").e("redirectUrl=> ${otpResponse.redirectUrl}")
                        _redirectUrl.postValue(otpResponse.redirectUrl)
                        isSuccess.value = true
                    }
                    is ResultWithMessage.Error -> {
                        Timber.tag("DEBUG").e("loadRequestOTP Error")
                        if (res.refresh) {
                            Timber.tag("DEBUG").e("loadRequestOTP REFRESH TOKEN")
                            utils.saveUserTokenPack("", true)
                            isSuccess.value = false
                        } else {
                            Timber.tag("DEBUG").e("Error message:: ${res.message}")
                            errorMessage.value = res.message
                        }
                        loading.value = false
                        // isSuccess.value = false
                        _isLoading.value = false
                    }
                }

            }, Timber::e)
            .addTo(disposable)
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
                    userMobileNum = it.subscriberNumber
                    if (!it.accessToken.isNullOrEmpty()) {
                        subscribeRequestOtp(mobileNum = it.subscriberNumber)
                    }
                }
            }, Timber::e)
            .addTo(disposable)
    }

    fun subscribeSubmitOTP(otp: String?) {
        Timber.tag("DEBUG").e("OTP CODE:: $otp")
        Timber.tag("DEBUG").e("OTP userMobileNum:: $userMobileNum")
        if (otp.isNullOrEmpty() || userMobileNum.isNullOrEmpty()) return

        repository.loadSubmitOTP(otp, userMobileNum!!)
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