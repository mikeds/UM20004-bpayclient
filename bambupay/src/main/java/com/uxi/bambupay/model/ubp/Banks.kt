package com.uxi.bambupay.model.ubp

/**
 * Created by Eraño Payawal on 8/15/20.
 * hunterxer31@gmail.com
 */
open class Banks {

    var records: ArrayList<Bank>? = null

}

open class Bank {

    var code: Long? = 0

    var bank: String? = null

    var brstn: Long? = 0

}