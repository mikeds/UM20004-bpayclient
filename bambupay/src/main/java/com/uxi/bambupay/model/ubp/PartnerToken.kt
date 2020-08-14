package com.uxi.bambupay.model.ubp

import com.google.gson.annotations.SerializedName

/**
 * Created by Eraño Payawal on 8/14/20.
 * hunterxer31@gmail.com
 */
open class PartnerToken {

    @SerializedName("token_type")
    var tokenType: String? = null

    @SerializedName("access_token")
    var accessToken: String? = null

    var metadata: String? = null

    @SerializedName("expires_in")
    var expiresIn: Long? = null

    @SerializedName("consented_on")
    var consentedOn: Long? = null

    var scope: String? = null

    @SerializedName("refresh_token")
    var refreshToken: String? = null

    @SerializedName("refresh_token_expires_in")
    var refreshTokenExpiresIn: Long? = null

}