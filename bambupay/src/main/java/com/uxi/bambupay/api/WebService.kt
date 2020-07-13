package com.uxi.bambupay.api

import com.uxi.bambupay.model.*
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

    @POST("client/cash-in")
    fun cashIn(@Body params: Request): Flowable<GenericApiResponse<CashIn>>

    @POST("client/send-to")
    fun sendTo(@Body params: Request): Flowable<GenericApiResponse<Pay>>

    @POST("client/cash-out")
    fun cashOut(@Body params: Request): Flowable<GenericApiResponse<CashOut>>

    @GET("client/history")
    fun history(): Flowable<GenericApiResponse<Transactions>>

}