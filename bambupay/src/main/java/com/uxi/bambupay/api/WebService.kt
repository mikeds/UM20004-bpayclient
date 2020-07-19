package com.uxi.bambupay.api

import com.uxi.bambupay.model.*
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WebService {

    @POST("token")
    fun getToken(@Body map: HashMap<String, String>): Flowable<TokenResponse>

    @POST("client/login")
    fun login(@Body params: Request): Flowable<GenericApiResponse<User>>

    @GET("client/balance")
    fun balance(): Flowable<GenericApiResponse<Balance>>

    @POST("client/cash-in")
    fun cashIn(@Body params: Request): Flowable<GenericApiResponse<CashIn>>

    @POST("client/send-to")
    fun sendTo(@Body params: Request): Flowable<GenericApiResponse<Pay>>

    @POST("client/cash-out")
    fun cashOut(@Body params: Request): Flowable<GenericApiResponse<CashOut>>

    @GET("client/history/")
    fun history(): Flowable<GenericApiResponse<Transactions>>

    @GET("client/history/{transactionId}")
    fun historyMore(@Path("transactionId") transactionId: Long): Flowable<GenericApiResponse<Transactions>>

    @GET("client/history")
    fun recentTransactions(): Flowable<GenericApiResponse<RecentTransactions>>

}