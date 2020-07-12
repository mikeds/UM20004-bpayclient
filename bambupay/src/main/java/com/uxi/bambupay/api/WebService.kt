package com.uxi.bambupay.api

import com.uxi.bambupay.model.Balance
import com.uxi.bambupay.model.TokenResponse
import com.uxi.bambupay.model.User
import io.reactivex.Flowable
import retrofit2.http.*

interface WebService {

    @POST("token")
    fun getToken(@Body map: HashMap<String, String>): Flowable<TokenResponse>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("client/login")
    fun login(@Field("username") username: String, @Field("password") password: String): Flowable<GenericApiResponse<User>>

    @GET("client/balance")
    fun balance(): Flowable<GenericApiResponse<Balance>>

}