package com.uxi.bambupay.repository

import com.uxi.bambupay.api.GenericApiResponse
import com.uxi.bambupay.model.ResultWithMessage
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection

/**
 * Created by Eraño Payawal on 12/22/20.
 * hunterxer31@gmail.com
 */
abstract class BaseRepository {

    protected fun errorHandler(t: Throwable): ResultWithMessage.Error {
        Timber.tag("Error").e(t)
        if (t is HttpException) {
            t.response()?.let { res ->
                var errorMessage: String? = null
                res.errorBody()?.let { body ->
                    val responseBody = GenericApiResponse.create<Any>(body.string())
                    when (responseBody.errorMessage) {
                        null -> Timber.tag("Error").e("empty error message")
                        else -> errorMessage = responseBody.errorMessage
                    }
                }
                return when (res.code()) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> ResultWithMessage.Error(true, errorMessage, t)
                    HttpURLConnection.HTTP_FORBIDDEN, HttpURLConnection.HTTP_INTERNAL_ERROR, HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> ResultWithMessage.Error(false, errorMessage, t)
                    else -> ResultWithMessage.Error(false, errorMessage, t)
                }
            }
        }
        return ResultWithMessage.Error(false, null)
    }

}