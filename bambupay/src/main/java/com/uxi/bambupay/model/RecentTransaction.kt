package com.uxi.bambupay.model

open class RecentTransaction {

    constructor(
        transactionType: String?,
        createdAt: Long?,
        transactionAmount: String?,
        mobileNumber: String?,
        message: String?,
        referenceID: String?
    ) {
        this.transactionType = transactionType
        this.createdAt = createdAt
        this.transactionAmount = transactionAmount
        this.mobileNumber = mobileNumber
        this.message = message
        this.referenceID = referenceID
    }

    var id: Int? = 0
    var transactionType: String? = null
    var createdAt: Long? = null
    var transactionAmount: String? = null
    var mobileNumber: String? = null
    var message: String? = null
    var referenceID: String? = null

}