package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uxi.bambupay.api.GenericApiResponse
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import timber.log.Timber

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
open class BaseViewModel : ViewModel() {

    protected var disposable: CompositeDisposable? = null
    val error = MutableLiveData<Error>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>(false)

    init {
        disposable = CompositeDisposable()
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable?.clear()
            disposable = null
        }
    }

    fun refreshToken(error: Throwable) : Boolean {
        if (error is HttpException) {
            error.response()?.let { res ->
                return when {
                    res.code() == 403 || res.code() == 401 -> {
                        Log.e("DEBUG", "token expired.")
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
        return false
    }

    fun errorHandling(error: Throwable) {
        if (error is HttpException) {
            error.response()?.let { res ->
                res.errorBody()?.let { body ->
                    val responseBody = GenericApiResponse.create<Any>(body.string())
                    when (responseBody.error) {
                        true -> {
                            Log.e("DEBUG", "ERROR message:: ${responseBody.message}")
                        }
                        else -> Timber.e(error)
                    }
                }
            }
        }
    }

}