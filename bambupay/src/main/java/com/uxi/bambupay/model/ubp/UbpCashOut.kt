package com.uxi.bambupay.model.ubp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eraño Payawal on 12/28/20.
 * hunterxer31@gmail.com
 */
open class UbpCashOut : Serializable {

    @SerializedName("sender_ref_id")
    var senderRefId: String? = null

    @SerializedName("qr_code")
    var qrCode: String? = null

    var timestamp: String? = null

}