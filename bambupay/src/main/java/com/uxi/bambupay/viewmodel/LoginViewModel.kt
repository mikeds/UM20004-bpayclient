package com.uxi.bambupay.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.repository.LoginRepository
import com.uxi.bambupay.utils.Utils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
class LoginViewModel @Inject
constructor(private val repository: LoginRepository, private val utils: Utils) : BaseViewModel() {

    val isSuccessLoggedIn = MutableLiveData<Boolean>()
    val refreshLogin = MutableLiveData<Boolean>()

    fun subscribeToken() {
        Log.e("DEBUG", "TOKEN=> ${utils.token}")
        Log.e("DEBUG", "isTokenExpired=> ${utils.isTokenExpired}")

        if (utils.token?.isNotEmpty()!! && !utils.isTokenExpired) return

        disposable?.add(repository.loadToken()
            .subscribe({
                it.let { token ->
                    Log.e("DEBUG", "accessToken:: ${token.accessToken}")
                    utils.saveTokenPack(token.accessToken, false)

                    if (isSuccessLoggedIn.value == false) {
                        refreshLogin.value = true
                    }
                }
            }, {
                Timber.e(it)
            })
        )
    }

    fun subscribeLogin(username: String, password: String) {
        if (isValidateCredentials(username, password)) {

            disposable?.add(repository.loadLogin(username, password)
                .doOnSubscribe { loading.value = true }
                .doAfterTerminate { loading.value = false }
                .subscribe({
                    Log.e("DEBUG", "User:: $it")
                    repository.saveUser(it)
                    utils.saveLoggedIn(true)
                    isSuccessLoggedIn.value = true
                }, {
                    Timber.e(it)
                    if (refreshToken(it)) {
                        Log.e("DEBUG", "error refreshToken")
                        utils.saveTokenPack("", true)
                        isSuccessLoggedIn.value = false
                    }
                })
            )

        }
    }

    private fun isValidateCredentials(username: String, password: String) : Boolean {
        if (username.isEmpty()) {
            // add error message
            return false
        }

        if (password.isEmpty()) {
            // add error message
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            // add error message
            return false
        }

        return true
    }

}