package com.uxi.bambupay.api

import com.google.gson.annotations.SerializedName

/**
 * Created by Eraño Payawal on 7/19/20.
 * hunterxer31@gmail.com
 */
open class RequestRegister private constructor(
    @SerializedName("first_name")
    private val firstName: String?,
    @SerializedName("last_name")
    private val lastName: String?,
    @SerializedName("email_address")
    private val email: String?,
    @SerializedName("mobile_country_code")
    private val mobileCountryCode: String?,
    @SerializedName("mobile_no")
    private val mobileNumber: String?,
    private val password: String?) {

    data class Builder(
        @SerializedName("first_name")
        private var firstName: String? = null,
        @SerializedName("last_name")
        private var lastName: String? = null,
        @SerializedName("email_address")
        private var email: String? = null,
        @SerializedName("mobile_country_code")
        private var mobileCountryCode: String? = null,
        @SerializedName("mobile_no")
        private var mobileNumber: String? = null,
        private var password: String? = null) {

        fun setFirstName(firstName: String) = apply { this.firstName = firstName }
        fun setLastName(lastName: String) = apply { this.lastName = lastName }
        fun setEmail(email: String) = apply { this.email = email }
        fun setMobileCountryCode(mobileCountryCode: String?) = apply { this.mobileCountryCode = mobileCountryCode }
        fun setMobileNumber(mobileNumber: String) = apply { this.mobileNumber = mobileNumber }
        fun setPassword(password: String) = apply { this.password = password }
        fun build() = RequestRegister(firstName, lastName, email, mobileCountryCode, mobileNumber, password)
    }

}