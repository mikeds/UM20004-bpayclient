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
    private val sentTo: String?
) {

    data class Builder(
        private var merchant: String? = null,
        private var amount: String? = null,
        @SerializedName("send_to")
        private var sentTo: String?
    ) {

        fun setMerchant(merchant: String) = apply { this.merchant = merchant }
        fun setAmount(amount: String) = apply { this.amount = amount }
        fun setSendTo(sentTo: String?) = apply { this.sentTo = sentTo }

        fun build() = Request(merchant, amount, sentTo)
    }

}