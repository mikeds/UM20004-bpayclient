package com.uxi.bambupay.model.lookup

import com.google.gson.annotations.SerializedName

/**
 * Created by Eraño Payawal on 3/19/21.
 * hunterxer31@gmail.com
 */
class TxDetails {

    var id: String? = null

    @SerializedName("ref_id")
    var refId: String? = null

    var amount: String? = null

    var fee: String? = null

    var status: String? = null

    var timestamp: String? = null

}