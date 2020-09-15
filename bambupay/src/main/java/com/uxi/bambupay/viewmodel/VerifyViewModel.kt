package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.api.Request
import com.uxi.bambupay.repository.VerifyRepository
import com.uxi.bambupay.utils.Utils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/19/20.
 * hunterxer31@gmail.com
 */
class VerifyViewModel @Inject
constructor(private val repository: VerifyRepository, private val utils: Utils) : BaseViewModel() {

    val isEmailEmpty = MutableLiveData<Boolean>()
    val isCodeEmpty = MutableLiveData<Boolean>()

    fun subscribeResendVerification(email: String?) {
        if (email.isNullOrEmpty() || !utils.isEmailValid(email)) {
            isEmailEmpty.value = true
            return
        }

        val requestBody = Request.Builder()
            .setUsername(email).build()

        disposable?.add(repository.loadResendVerification(requestBody)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({

                if (it.response != null) {

                } else {
                    if (it.error == true) {
                        it.message?.let { error ->
                            errorMessage.value = error
                            Log.e("DEBUG", "error message:: $error")
                        }
                        it.successMessage?.let { error ->
                            errorMessage.value = error
                        }
                    } else {
                        isSuccess.value = true
                    }
                }

            }, {
                Timber.e(it)
                if (refreshToken(it)) {
                    Log.e("DEBUG", "error refreshToken")
                    utils.saveTokenPack("", true)
                    isSuccess.value = false
                }
            })
        )

    }

    fun subscribeVerifyCode(code: String?, email: String?) {

        if (code.isNullOrEmpty()) {
            isCodeEmpty.value = true
            return
        }

        val requestBody = Request.Builder()
            .setCode(code)
            .setUsername(email!!).build()

        disposable?.add(repository.loadVerifyCode(requestBody)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({

                if (it.response != null) {

                } else {
                    if (it.error == true) {
                        it.message?.let { error ->
                            errorMessage.value = error
                            Log.e("DEBUG", "error message:: $error")
                        }
                        it.successMessage?.let { error ->
                            errorMessage.value = error
                        }
                    } else {
                        isSuccess.value = true
                    }
                }

            }, {
                Timber.e(it)
                if (refreshToken(it)) {
                    Log.e("DEBUG", "error refreshToken")
                    utils.saveTokenPack("", true)
                    isSuccess.value = false
                }
            })
        )

    }

}