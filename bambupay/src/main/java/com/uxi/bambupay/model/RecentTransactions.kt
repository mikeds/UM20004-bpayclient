package com.uxi.bambupay.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RecentTransactions : RealmObject() {

    @SerializedName("last_transaction_id")
    var lastTransactionId: Long? = null

    var history: RealmList<RecentTransaction>? = null

}

open class RecentTransaction : RealmObject() {

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