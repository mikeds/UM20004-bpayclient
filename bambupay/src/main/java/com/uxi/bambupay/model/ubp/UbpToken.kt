package com.uxi.bambupay.model.ubp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eraño Payawal on 12/28/20.
 * hunterxer31@gmail.com
 */
open class UbpToken : Serializable {

    @SerializedName("access_token")
    var accessToken: String? = null

    var timestamp: String? = null

}