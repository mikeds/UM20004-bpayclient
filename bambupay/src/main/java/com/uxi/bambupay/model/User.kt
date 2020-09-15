package com.uxi.bambupay.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
open class User : RealmObject() {

    @SerializedName("email_address")
    @PrimaryKey
    var emailAddress: String? = null

    @SerializedName("first_name")
    var firstName: String? = null

    @SerializedName("middle_name")
    var middleName: String? = null

    @SerializedName("last_name")
    var lastName: String? = null

    @SerializedName("mobile_no")
    var mobileNumber: String? = null

    @SerializedName("secret_key")
    var secretKey: String? = null

    @SerializedName("secret_code")
    var secretCode: String? = null

    @SerializedName("qr_code")
    var qrCode: String? = null

    @SerializedName("avatar_image")
    var avatarImageUrl: String? = null

    @SerializedName("account_status")
    var accountStatus: String? = null

    @SerializedName("email_status")
    var emailStatus: String? = null

}