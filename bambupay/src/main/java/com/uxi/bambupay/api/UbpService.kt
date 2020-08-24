package com.uxi.bambupay.api

import com.uxi.bambupay.model.ubp.Banks
import com.uxi.bambupay.model.ubp.PartnerToken
import com.uxi.bambupay.model.ubp.RequestTransfer
import com.uxi.bambupay.model.ubp.SingleTransfer
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * Created by Eraño Payawal on 8/14/20.
 * hunterxer31@gmail.com
 */
interface UbpService {

    @FormUrlEncoded
    @POST("partners/sb/customers/v1/oauth2/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun partnerCustomerAccessToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): Flowable<PartnerToken>

    @GET("partners/sb/partners/v3/instapay/banks")
    fun instapayPartners(
        @Header("x-ibm-client-id") clientId: String,
        @Header("x-ibm-client-secret") clientSecret: String,
        @Header("x-partner-id") partnerId: String
    ): Flowable<Banks>

    @POST("partners/sb/online/v2/transfers/single")
    fun instapayPartnerUbpSingleTransfer(@Header("x-ibm-client-id") clientId: String,
                                         @Header("x-ibm-client-secret") clientSecret: String,
                                         @Header("x-partner-id") partnerId: String,
                                         @Header("authorization") authorization: String,
                                         @Body requestBody: RequestTransfer): Flowable<SingleTransfer>

}