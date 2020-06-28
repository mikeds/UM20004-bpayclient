package com.uxi.bambupay.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
class TokenResponse {

    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("expires_in")
    var expires: Long? = null

    @SerializedName("token_type")
    var tokenType: String? = null

    var scope: String? = null

}