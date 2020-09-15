package com.uxi.bambupay.api

import com.google.gson.annotations.SerializedName

/**
 * Created by Eraño Payawal on 7/13/20.
 * hunterxer31@gmail.com
 */
open class Request private constructor(
    private val merchant: String?,
    private val amount: String?,
    @SerializedName("send_to")
    private val sentTo: String?,
    private val username: String?,
    private val password: String?,
    private val code: String?,
    @SerializedName("email_address")
    private val email: String?) {

    data class Builder(
        private var merchant: String? = null,
        private var amount: String? = null,
        @SerializedName("send_to")
        private var sentTo: String? = null,
        private var username: String? = null,
        private var password: String? = null,
        private var code: String? = null,
        @SerializedName("email_address")
        private var email: String? = null) {

        fun setUsername(username: String) = apply { this.username = username }
        fun setCode(code: String) = apply { this.code = code }
        fun setPassword(password: String?) = apply { this.password = password }
        fun setMerchant(merchant: String) = apply { this.merchant = merchant }
        fun setAmount(amount: String) = apply { this.amount = amount }
        fun setSendTo(sentTo: String?) = apply { this.sentTo = sentTo }
        fun setEmail(email: String?) = apply { this.email = email }

        fun build() = Request(merchant, amount, sentTo, username, password, code, email)
    }

}