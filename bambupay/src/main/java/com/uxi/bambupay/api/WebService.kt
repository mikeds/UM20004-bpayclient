package com.uxi.bambupay.api

import com.uxi.bambupay.model.*
import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @GET("clients/transactions")
    fun history(): Flowable<GenericApiResponse<Transactions>>

    @GET("clients/transactions/{transactionId}")
    fun historyMore(@Path("transactionId") transactionId: String): Flowable<GenericApiResponse<Transactions>>

    @GET("clients/transactions")
    fun recentTransactions(): Flowable<GenericApiResponse<RecentTransactions>>

    @Multipart
    @POST("clients/registration")
    fun register(@PartMap() partMap: Map<String, @JvmSuppressWildcards RequestBody>): Flowable<GenericApiResponse<User>>

    // Working
    @Multipart
    @POST("clients/registration")
    fun registerWithFile(@PartMap() partMap: Map<String, @JvmSuppressWildcards RequestBody>, @Part file: MultipartBody.Part?): Flowable<GenericApiResponse<User>>

    @POST("activation/client-email/activate")
    fun verificationCode(@Body params: Request): Flowable<GenericApiResponse<User>>

    @POST("activation/client-email/resend")
    fun resendVerificationCode(@Body params: Request): Flowable<GenericApiResponse<User>>

    @GET("tools/provinces/169")
    fun getProvinces(): Flowable<GenericApiResponse<List<Province>>>

    @POST("transactions/client/scanpayqr")
    fun scanPayQR(@Body params: Request): Flowable<GenericApiResponse<User>>

    @GET("transactions/client/quickpayqr/scan/{merchantId}")
    fun quickPayQRMerchant(@Path("merchantId") merchantId: String): Flowable<GenericApiResponse<MerchantInfo>>

    @POST("transactions/client/quickpayqr/accept")
    fun quickPayQRAccept(@Body params: Request): Flowable<GenericApiResponse<ScanQr>>

    @POST("transactions/client/createpayqr/create")
    fun createPayQr(@Body params: Request): Flowable<GenericApiResponse<ScanQr>>

    @POST("transactions/client/scanpayqr/accept")
    fun acceptPayQr(@Body params: Request): Flowable<GenericApiResponse<ScanQr>>

    @GET("lookup/client/tx-fee")
    fun transactionFee(
        @Query("tx_type_id") txTypeId: String,
        @Query("amount") amount: String
    ): Flowable<GenericApiResponse<FeeResponse>>

}