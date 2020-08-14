package com.uxi.bambupay.api

import com.uxi.bambupay.model.ubp.Banks
import com.uxi.bambupay.model.ubp.PartnerToken
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * Created by Eraño Payawal on 8/14/20.
 * hunterxer31@gmail.com
 */
interface UbpService {

    @FormUrlEncoded
    @POST("partners/sb/partners/v1/oauth2/token")
    fun partnerAccessToken(
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("scope") scope: String,
        @Field("client_id") clientId: String
    ): Flowable<PartnerToken>

    @GET("partners/sb/partners/v3/instapay/banks")
    fun instapayPartners(
        @Header("x-ibm-client-id") clientId: String,
        @Header("x-ibm-client-secret") clientSecret: String,
        @Header("x-partner-id") partnerId: String
    ): Flowable<Banks>

}