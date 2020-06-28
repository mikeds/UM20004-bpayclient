package com.uxi.bambupay.api

import com.uxi.bambupay.model.TokenResponse
import com.uxi.bambupay.model.User
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.POST

interface WebService {

    @POST("token")
    fun getToken(@Body map: HashMap<String, String>): Flowable<TokenResponse>

    @POST("client/login")
    fun login(@Body map: HashMap<String, String>): Flowable<User>

}