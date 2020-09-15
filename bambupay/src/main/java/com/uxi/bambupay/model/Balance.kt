package com.uxi.bambupay.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 * Created by Eraño Payawal on 7/13/20.
 * hunterxer31@gmail.com
 */
open class Balance : RealmObject() {

    @SerializedName("account_number")
    var accountNumber: String? = null

    @SerializedName("balance")
    var balance: String? = null

    @SerializedName("hold_balance")
    var holdBalance: String? = null

}