package com.uxi.bambupay.utils

class Constants {
    companion object {
        const val SEND_MONEY = "Transfer"
        const val CASH_IN = "Cash-in (OTC)"
        const val CASH_OUT = "cash_out"
        const val SCAN_PAY_QR = "ScanPayQR"
        const val CREATE_SCAN_QR = "CreateScanQR"
        const val QUICK_PAY_QR = "QuickPayQR"

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

        // TRANSACTION TYPE ID
        const val TX_TYPE_CASH_IN_OTC_ID = "txtype_cashin1"
        const val TX_TYPE_CASH_IN_DP_ID = "txtype_cashin2"
        const val TX_TYPE_CASH_OUT_OTC_ID = "txtype_cashout1"
        const val TX_TYPE_CREATE_SCAN_QR_ID = "txtype_createpayqr1"
        const val TX_TYPE_EN_CASH_ID = "txtype_encash1"
        const val TX_TYPE_INCOME_SHARES_ID = "txtype_income_shares"
        const val TX_TYPE_QUICK_PAY_QR_ID = "txtype_quickpayqr1"
        const val TX_TYPE_SCAN_PAY_QR_ID = "txtype_scanpayqr1"
        const val TX_TYPE_TOP_UP_OTC_ID = "txtype_topup1"
        const val TX_TYPE_TRANSFER_ID = "txtype_transfer1"
        const val TX_TYPE_VAULT_ID = "txtype_vault1"
    }
}