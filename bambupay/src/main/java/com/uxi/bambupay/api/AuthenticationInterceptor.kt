package com.uxi.bambupay.api

import android.util.Log
import com.uxi.bambupay.BuildConfig
import com.uxi.bambupay.utils.Utils
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
class AuthenticationInterceptor
@Inject constructor(val utils: Utils?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder: Request.Builder

        if (BuildConfig.DEBUG) {
            Timber.tag("DEBUG").e("TOKEN:: ${utils?.token}")
        }

        var token = ""
        utils?.let {
            if (it.token.isNullOrBlank() && it.isTokenExpired) {
                val basicAuth = getBasicAuth()
                token = "".plus(basicAuth)
            } else {
                token = "Bearer ${it.token}"
                if (BuildConfig.DEBUG) {
                    Log.e("DEBUG", "Bearer Token:: $token")
                }
            }
        }

        builder = original.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .header("Authorization", token)
        val request = builder.build()
        return chain.proceed(request)
    }

    private fun getBasicAuth() : String {
        val username = "testclientid"
        val password = "testsecretcode"
        return Credentials.basic(username, password)
    }
}