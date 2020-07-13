package com.uxi.bambupay.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Eraño Payawal on 7/13/20.
 * hunterxer31@gmail.com
 */
open class Transactions : RealmObject() {

    @SerializedName("last_transaction_id")
    var lastTransactionId: Long? = null

    var history: RealmList<Transaction>? = null

}

open class Transaction : RealmObject() {

    @SerializedName("transaction_id")
    @PrimaryKey
    var transactionId: Long? = null

    @SerializedName("transaction_number")
    var transactionNumber: String? = null

    @SerializedName("transactionn_qr_code")
    var transactionQrCode: String? = null

    var status: String? = null

    var type: String? = null

    var amount: String? = null

    var date: String? = null

    @SerializedName("date_expiration")
    var dateExpiration: String? = null

}