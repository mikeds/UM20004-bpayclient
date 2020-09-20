package com.uxi.bambupay.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RecentTransactions : RealmObject() {

    @PrimaryKey
    @SerializedName("last_id")
    var lastId: String? = null

    var data: RealmList<RecentTransaction>? = null

}

open class RecentTransaction : RealmObject() {

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


    /*@SerializedName("transaction_number")
    var transactionNumber: String? = null

    @SerializedName("transactionn_qr_code")
    var transactionQrCode: String? = null

    var status: String? = null

    var type: String? = null

    var amount: String? = null

    var date: String? = null

    @SerializedName("date_expiration")
    var dateExpiration: String? = null
*/
}