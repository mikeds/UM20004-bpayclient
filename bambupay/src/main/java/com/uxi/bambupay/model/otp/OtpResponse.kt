package com.uxi.bambupay.model.otp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eraño Payawal on 12/1/20.
 * hunterxer31@gmail.com
 */
class OtpResponse : Serializable {

    var error: Boolean = false

    @SerializedName("error_description")
    var errorDescription: String? = null

    @SerializedName("redirect_url")
    var redirectUrl: String? = null

}