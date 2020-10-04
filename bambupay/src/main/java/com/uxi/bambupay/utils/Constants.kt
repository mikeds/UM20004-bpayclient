package com.uxi.bambupay.utils

class Constants {
    companion object {
        const val SEND_MONEY = "Transfer"
        const val CASH_IN = "Cash-in (OTC)"
        const val CASH_OUT = "cash_out"
        const val SCAN_PAY_QR = "ScanPayQR"

        const val PENDING = "pending"
        const val APPROVED = "approved"
        const val CANCELLED = "cancelled"

        const val PASSWORD_MIN_LENGTH = 8

        const val DEBIT = "debit"
        const val CREDIT = "credit"

        const val TYPE_OTC = "otc"

        const val AMOUNT = "amount"
        const val SCREEN_FROM = "screen_from"
        const val QUICK_SCAN_SCREEN = "screen_from"
    }
}