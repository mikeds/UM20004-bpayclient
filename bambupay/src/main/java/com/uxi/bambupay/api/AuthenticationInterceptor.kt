package com.uxi.bambupay.api

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

        Timber.tag("DEBUG").e("TOKEN:: ${utils?.token}")
        Timber.tag("DEBUG").e("USER TOKEN:: ${utils?.userToken}")

        var token = ""
        utils?.let {
            if (!it.isLoggedIn) {
                token = if (it.token.isNullOrBlank() && it.isTokenExpired) {
                    // this is for generating token for login
                    val basicAuth = getBasicAuth()
                    "".plus(basicAuth)
                } else {
                    // this is for login only
                    "Bearer ${it.token}"
                }
            } else {
                token = if (it.userToken.isNullOrBlank() && it.isUserTokenExpired) {
                    val userAuth = Credentials.basic(it.userSecretCode!!, it.userSecretKey!!)
                    Timber.tag("DEBUG").e("userAuth:: $userAuth")
                    "".plus(userAuth)
                } else {
                    "Bearer ${it.userToken}"
                }
            }
        }

        if (BuildConfig.DEBUG) {
            Timber.tag("Authentication").e("Bearer Token:: $token")
        }

        builder = original.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .header("Authorization", token)
        val request = builder.build()
        return chain.proceed(request)
    }

    private fun getBasicAuth() : String {
//        val username = "ecf3662266040ba80bfa49d925f74999e56d7f80f0b3b14e430a5d526c591dcf"
//        val password = "9326fdf58cfe187a90ba4a1c7bcabbbdb72afb1ad0495472a1befa6c4a0c61f9"
        val username = "dc18e7f96c1704c90a2e58a528cd944e06b26ccf237aa12aba335b62e11a9b20"
        val password = "0246d7444d1407e9ad2ea42f7b4aa4bf2d1ead63131cb4c0c72badfa218f2a6e"
        return Credentials.basic(username, password)
    }

}