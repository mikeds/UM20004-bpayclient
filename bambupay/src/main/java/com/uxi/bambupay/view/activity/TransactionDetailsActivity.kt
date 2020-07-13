package com.uxi.bambupay.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.uxi.bambupay.R
import com.uxi.bambupay.model.RecentTransactions
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.utils.getDateTimeFormat
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_transaction_details.*

class TransactionDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        val transaction = intent.getSerializableExtra("transaction") as? RecentTransactions
        setViews(transaction)
    }

    override fun getLayoutId() = R.layout.activity_transaction_details

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        tv_toolbar_title?.text = getString(R.string.transaction_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun  setViews(transactions: RecentTransactions?) {
//        transactions?.let {
//            val transactionType = it.transactionType
//            if (transactionType.equals(Constants.SEND_MONEY)) {
//                txt_transaction_type?.text = getString(R.string.send_money)
//                txt_amount?.setTextColor(ContextCompat.getColor(this, R.color.red))
//            } else if (transactionType.equals(Constants.CASH_IN)) {
//                txt_transaction_type?.text = getString(R.string.cash_in)
//                txt_amount?.setTextColor(ContextCompat.getColor(this, R.color.light_green))
//            }
//            txt_amount?.text = it.transactionAmount
//            val dateTime = getDateTimeFormat(it.createdAt)
//            txt_date?.text = dateTime
//            txt_mobile_number?.text = it.mobileNumber
//            txt_message?.text = it.message
//            txt_reference_id?.text = it.referenceID
//        }
    }
}