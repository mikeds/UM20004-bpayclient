package com.uxi.bambupay.model.otp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eraño Payawal on 12/1/20.
 * hunterxer31@gmail.com
 */
class OtpTokenResponse : Serializable {

    @SerializedName("access_token")
    var accessToken: String? = null

    @SerializedName("subscriber_number")
    var subscriberNumber: String? = null

}