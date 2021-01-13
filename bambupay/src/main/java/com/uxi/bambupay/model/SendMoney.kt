package com.uxi.bambupay.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eraño Payawal on 1/13/21.
 * hunterxer31@gmail.com
 */
open class SendMoney : Serializable {

    @SerializedName("sender_ref_id")
    var senderRefId: String? = null

    var timestamp: String? = null

}