package com.uxi.bambupay.view.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.model.RecentTransactions
import com.uxi.bambupay.model.Transaction
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.utils.convertTimeToDate
import com.uxi.bambupay.utils.getDateTimeFormat
import com.uxi.bambupay.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_transaction_details.*

class TransactionDetailsActivity : BaseActivity() {

    private val transactionViewModel by viewModel<TransactionViewModel>()
    private var transactionId: Long? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

        transactionId = intent?.getLongExtra("transactionId", 0)

        observeViewModel()

//        val transaction = intent.getSerializableExtra("transaction") as? RecentTransactions
//        val transaction = intent.getSerializableExtra("transaction") as? Transaction
//        setViews(transaction)
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

    private fun observeViewModel() {
        if (transactionId!! > 0) {
            transactionViewModel.subscribeTransactionId(transactionId)
        }

        transactionViewModel.transactionData.observe(this, Observer { transactionData ->
            transactionData?.let {
                setViews(it)
            }

        })
    }

    private fun setViews(transaction: Transaction?) {
        val transactionType = transaction?.type
        when {
            transactionType.equals(Constants.SEND_MONEY) -> {
                txt_transaction_type.text = getString(R.string.send_money)
                txt_amount?.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
            transactionType.equals(Constants.CASH_IN) -> {
                txt_transaction_type.text = getString(R.string.cash_in)
                txt_amount?.setTextColor(ContextCompat.getColor(this, R.color.light_green))
            }
            transactionType.equals(Constants.CASH_OUT) -> {
                txt_transaction_type.text = getString(R.string.cash_out)
                txt_amount?.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
        }

        when (transaction?.status) {
            Constants.PENDING -> {
                txt_status?.text = getString(R.string.pending)
            }
            Constants.APPROVED -> {
                txt_status?.text = getString(R.string.approved)
                txt_status?.setTextColor(ContextCompat.getColor(this, R.color.light_green))
            }
            Constants.CANCELLED -> {
                txt_status?.text = getString(R.string.cancelled)
                txt_status?.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
        }

        val transactionAmount = "PHP ${transaction?.amount}"
        txt_amount?.text = transactionAmount
        val dateTime = convertTimeToDate(transaction?.date)
        txt_date?.text = dateTime
//        txt_mobile_number?.text = it.mobileNumber
//        txt_message?.text = it.message
        txt_reference_id?.text = transaction?.transactionNumber
    }
}