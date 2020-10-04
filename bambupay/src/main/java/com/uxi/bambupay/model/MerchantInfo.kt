package com.uxi.bambupay.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eraño Payawal on 10/5/20.
 * hunterxer31@gmail.com
 */
class MerchantInfo : Serializable {

    @SerializedName("account_number")
    var accountNumber: String? = null

    @SerializedName("account_fname")
    var accountFname: String? = null

    @SerializedName("account_mname")
    var accountMname: String? = null

    @SerializedName("account_lname")
    var accountLname: String? = null

    @SerializedName("account_email_address")
    var accountEmailAddress: String? = null

}