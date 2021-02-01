package com.uxi.bambupay.utils

class Constants {
    companion object {
        const val SEND_MONEY = "Transfer"
        const val CASH_IN = "Cash-in (OTC)"
        const val CASH_OUT = "cash_out"
        const val SCAN_PAY_QR = "ScanPayQR"
        const val CREATE_SCAN_QR = "CreateScanQR"
        const val QUICK_PAY_QR = "QuickPayQR"
        const val CASH_IN_PAYNAMICS = "Cash-in (CC/DEBIT)"
        const val CASH_OUT_UBP_INSTAPAY = "Cash-out (UBP/INSTAPAY)"

        const val PENDING = "pending"
        const val APPROVED = "approved"
        const val CANCELLED = "cancelled"

        const val PASSWORD_MIN_LENGTH = 8

        const val DEBIT = "debit"
        const val CREDIT = "credit"

        const val TYPE_OTC = "otc"

        const val AMOUNT = "amount"

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

        const val SCREEN_FROM = "screen_from"
        const val CASH_IN_OTC_SCREEN = "cash_in_otc_screen"
        const val CASH_IN_BANK_SCREEN = "cash_in_bank_screen"
        const val CASH_IN_CARD_SCREEN = "cash_in_card_screen"
        const val CASH_IN_BANCNET_SCREEN = "cash_in_bancnet_screen"
        const val CASH_IN_GRAB_SCREEN = "cash_in_grab_screen"
        const val CASH_IN_GCASH_SCREEN = "cash_in_gcash_screen"
        const val CASH_IN_PAYMAYA_SCREEN = "cash_in_paymaya_screen"
        const val CASH_OUT_SCREEN = "cash_out_screen"
        const val SEND_MONEY_SCREEN = "send_money_screen"
        const val SCAN_PAY_QR_SCREEN = "scan_pay_qr_screen"
        const val CREATE_PAY_QR_SCREEN = "create_pay_qr_screen"
        const val QUICK_PAY_SCAN_SCREEN = "quick_pay_scan_screen"
        const val REGISTRATION_SCREEN = "registration_screen"

        const val REDIRECT_URL = "redirect_url"
        const val MOBILE_NUMBER = "mobile_number"
        const val BANK_CODE = "bank_code"
        const val ACCOUNT_NO = "account_no"
        const val MESSAGE = "message"
        const val RECIPIENT_NUMBER = "recipient_number"
        const val REF_ID_NUMBER = "refIdNumber"

        const val CASH_IN_PAYNAMICS_CC = "cc"
        const val CASH_IN_PAYNAMICS_BANCNET = "bancnet"
        const val CASH_IN_PAYNAMICS_GRAB_PAY = "grabpay"
        const val CASH_IN_PAYNAMICS_GCASH = "gcash"
        const val CASH_IN_PAYNAMICS_PAYMAYA = "paymaya"
        const val CASH_IN_REDIRECT_URL = "redirect_url"
    }
}