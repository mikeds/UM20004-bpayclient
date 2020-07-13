package com.uxi.bambupay.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Eraño Payawal on 7/13/20.
 * hunterxer31@gmail.com
 */
class Pay {

    @SerializedName("transaction_number")
    var transactionNumber: String? = null

    @SerializedName("transactionn_qr_code")
    var transactionQrCode: String? = null

    @SerializedName("send_to")
    var sendTo: String? = null

    var balance: String? = null

    var amount: String? = null

    @SerializedName("request_expiration")
    var requestExpiration: String? = null

}