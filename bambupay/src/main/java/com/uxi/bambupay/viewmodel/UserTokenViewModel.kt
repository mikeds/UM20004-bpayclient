package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.repository.UserTokenRepository
import com.uxi.bambupay.utils.Utils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/12/20.
 * hunterxer31@gmail.com
 */
class UserTokenViewModel @Inject
constructor(private val repository: UserTokenRepository, private val utils: Utils) : BaseViewModel() {

    val isTokenRefresh = MutableLiveData<Boolean>()

    fun subscribeToken() {

        Log.e("DEBUG", "isUserTokenExpired=> ${utils.isUserTokenExpired}")
        Log.e("DEBUG", "userToken=> ${utils.userToken}")
        if (utils.userToken?.isNotEmpty()!! && !utils.isUserTokenExpired)
            return

        disposable?.add(repository.loadToken()
            .subscribe({
                it.let { token ->
                    utils.saveUserTokenPack(token.accessToken, false)
                    isTokenRefresh.value = true
                }
            }, {
                Log.e("DEBUG", "error token")
                Timber.e(it)
            })
        )

    }

}