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

    @PrimaryKey
    @SerializedName("last_id")
    var lastId: String? = null

    var data: RealmList<Transaction>? = null

}

open class Transaction : RealmObject() {

    @PrimaryKey
    @SerializedName("tx_id")
    var id: String? = null

    @SerializedName("sender_ref_id")
    var senderRefId: String? = null

    @SerializedName("tx_created_by")
    var createdBy: String? = null

    @SerializedName("tx_from")
    var sender: String? = null

    @SerializedName("tx_to")
    var recipient: String? = null

    var amount: String? = null

    var fee: String? = null

    @SerializedName("tx_type")
    var transactionType: String? = null

    @SerializedName("date_created")
    var dateCreated: String? = null

    @SerializedName("tx_status")
    var status: String? = null

    @SerializedName("balance_type")
    var balanceType: String? = null

    @SerializedName("qr_code")
    var qrCode: String? = null

}