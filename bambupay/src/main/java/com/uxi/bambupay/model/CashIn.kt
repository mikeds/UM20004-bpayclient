package com.uxi.bambupay.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Eraño Payawal on 7/13/20.
 * hunterxer31@gmail.com
 */
class CashIn {

    @SerializedName("sender_ref_id")
    var senderRefId: String? = null

    @SerializedName("qr_code")
    var qrCode: String? = null

    var amount: String? = null

    var fee: Int = 0

    @SerializedName("total_amount")
    var totalAmount: Int = 0

    var timestamp: String? = null

}