package com.uxi.bambupay.model.ubp

/**
 * Created by Eraño Payawal on 8/24/20.
 * hunterxer31@gmail.com
 */
open class RequestTransfer private constructor(
    private val senderRefId: String?,
    private val tranRequestDate: String?,
    private val accountNo: String?,
    private val amount: RequestAmount?,
    private val remarks: String?,
    private val particulars: String?,
    private val info: MutableList<RequestInfo>?) {

    data class Builder(private var senderRefId: String? = null,
                       private var tranRequestDate: String? = null,
                       private var accountNo: String? = null,
                       private var amount: RequestAmount? = null,
                       private var remarks: String? = null,
                       private var particulars: String? = null,
                       private var info: MutableList<RequestInfo>) {

        constructor() : this(
            info = mutableListOf()
        )

        fun senderRefId(senderRefId: String) = apply { this.senderRefId = senderRefId }

        fun tranRequestDate(tranRequestDate: String) = apply { this.tranRequestDate = tranRequestDate }

        fun accountNo(accountNo: String) = apply { this.accountNo = accountNo }

        fun amount(currency: String, value: String) = apply {
            val amount = RequestAmount().apply {
                this.currency = currency
                this.value = value
            }
            this.amount = amount
        }

        fun remarks(remarks: String) = apply { this.remarks = remarks }

        fun particulars(particulars: String) = apply { this.particulars = particulars }

        fun info(index: Int, name: String, value: String) = apply {
            val info = RequestInfo().apply {
                this.index = index
                this.name = name
                this.value = value
            }
            this.info.add(info)
        }

        fun build() = RequestTransfer(senderRefId, tranRequestDate, accountNo, amount, remarks, particulars, info)

    }

}

open class RequestAmount {

    var currency: String? = null

    var value: String? = null

}

open class RequestInfo {

    var index: Int? = null

    var name: String? = null

    var value: String? = null

}