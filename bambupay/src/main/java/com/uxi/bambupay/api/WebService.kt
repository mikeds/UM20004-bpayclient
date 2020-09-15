package com.uxi.bambupay.api

import com.uxi.bambupay.model.*
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.*

interface WebService {

    @POST("token")
    fun getToken(@Body map: HashMap<String, String>): Flowable<TokenResponse>

//    @Headers("Content-Type: text/html; charset=utf-8")
    @POST("clients/login")
    fun login(@Body params: Request): Flowable<GenericApiResponse<User>>

    @GET("clients/balance")
    fun balance(): Flowable<GenericApiResponse<Balance>>

    @POST("transactions/client/cash-in")
    fun cashIn(@Body params: Request): Flowable<GenericApiResponse<CashIn>>

    @POST("transactions/client/send-to")
    fun sendTo(@Body params: Request): Flowable<GenericApiResponse<Pay>>

    @POST("client/cash-out")
    fun cashOut(@Body params: Request): Flowable<GenericApiResponse<CashOut>>

    @GET("clients/ledger")
    fun history(): Flowable<GenericApiResponse<Transactions>>

    @GET("clients/ledger/{transactionId}")
    fun historyMore(@Path("transactionId") transactionId: String): Flowable<GenericApiResponse<Transactions>>

    @GET("clients/ledger")
    fun recentTransactions(): Flowable<GenericApiResponse<RecentTransactions>>

    @POST("clients/registration")
    fun register(@Body params: RequestRegister): Flowable<GenericApiResponse<User>>

    @POST("client/code-confirmation")
    fun verificationCode(@Body params: Request): Flowable<GenericApiResponse<User>>

    @POST("client/resend-code-confirmation")
    fun resendVerificationCode(@Body params: Request): Flowable<GenericApiResponse<User>>

}