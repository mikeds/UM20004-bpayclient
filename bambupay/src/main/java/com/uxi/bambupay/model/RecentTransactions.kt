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
    var id: String? = null

    @SerializedName("transaction_id")
    var transactionId: String? = null

    @SerializedName("sender_ref_id")
    var senderRefId: String? = null

    @SerializedName("transaction_type")
    var transactionType: String? = null

    @SerializedName("transaction_code")
    var transactionCode: String? = null

    @SerializedName("transaction_requested_by")
    var transactionRequestedBy: String? = null

    @SerializedName("old_balance")
    var oldBalance: String? = null

    @SerializedName("debit_credit_amount")
    var debitCreditAmount: String? = null

    @SerializedName("new_balance")
    var newBalance: String? = null

    @SerializedName("date_added")
    var dateAdded: String? = null

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