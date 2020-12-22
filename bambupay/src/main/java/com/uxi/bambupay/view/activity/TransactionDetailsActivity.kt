package com.uxi.bambupay.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.uxi.bambupay.R
import com.uxi.bambupay.model.Transaction
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.utils.convertTimeToDate
import com.uxi.bambupay.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_transaction_details.*
import timber.log.Timber

class TransactionDetailsActivity : BaseActivity() {

    private val transactionViewModel by viewModel<TransactionViewModel>()
    private val transactionId by lazy {
        intent?.getStringExtra("transactionId")
    }

    private val historyType by lazy {
        intent?.getStringExtra("history_type")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

//        transactionId = intent?.getLongExtra("transactionId", 0)

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
        Timber.tag("DEBUG").e("transactionId:: $transactionId")
        if (!transactionId.isNullOrEmpty()) {
            transactionViewModel.subscribeTransactionId(transactionId, historyType)
        }

        transactionViewModel.transactionData.observe(this, Observer { transactionData ->
            transactionData?.let {
                setTransactionType(it.transactionType, it.balanceType)
                setTransactionAmount(it.amount)
                setDate(it.dateCreated)
                setReferenceNumber(it.senderRefId)
                setQrCode(it.qrCode)
                setStatus(it.status)
                setFee(it.fee)
                setSender(it.sender)
                setRecipient(it.recipient)
            }
        })

        transactionViewModel.transactionRecentData.observe(this, Observer { transactionData ->
            transactionData?.let {
                setTransactionType(it.transactionType, it.balanceType)
                setTransactionAmount(it.amount)
                setDate(it.dateCreated)
                setReferenceNumber(it.senderRefId)
                setQrCode(it.qrCode)
                setStatus(it.status)
                setFee(it.fee)
                setSender(it.sender)
                setRecipient(it.recipient)
            }
        })
    }

    private fun setTransactionType(transactionType: String?, balanceType: String?) {
        when (transactionType) {
            Constants.SEND_MONEY -> {
                txt_transaction_type.text = getString(R.string.send_money)
                if (balanceType == Constants.DEBIT) {
                    txt_amount?.setTextColor(ContextCompat.getColor(this, R.color.red))
                } else {
                    txt_amount?.setTextColor(ContextCompat.getColor(this, R.color.black))
                }
            }
            Constants.CASH_IN, Constants.CASH_IN_PAYNAMICS -> {
                txt_transaction_type.text = getString(R.string.cash_in)
                txt_amount?.setTextColor(ContextCompat.getColor(this, R.color.light_green))
            }
            Constants.CASH_OUT -> {
                txt_transaction_type.text = getString(R.string.cash_out)
                txt_amount?.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
            Constants.SCAN_PAY_QR -> {
                txt_transaction_type.text = getString(R.string.scan_pay)
                txt_amount?.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
        }
    }

    private fun setTransactionAmount(amount: String?) {
        amount?.let {
            val transactionAmount = transactionViewModel.formatAmount(it)
            txt_amount?.text = getString(R.string.ph_currency, transactionAmount)
        }
    }

    private fun setFee(fee: String?) {
        fee?.let {
            txt_fee.text = getString(R.string.ph_currency, fee)
        }
    }

    private fun setDate(dateTime: String?) {
        dateTime?.let {
            val convertedTime = convertTimeToDate(it)
            txt_date?.text = convertedTime
        }
    }

    private fun setReferenceNumber(refNo: String?) {
        refNo?.let {
            txt_reference_id?.text = it
        }
    }

    private fun setQrCode(imageUrl: String?) {
        imageUrl?.let {
            Glide.with(this).load(it)
                .thumbnail(1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image_qr)
        }
    }

    private fun setStatus(status: String?) {
        when (status) {
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
    }

    private fun setSender(sender: String?) {
        sender?.let {
            txt_sender.text = it
        }
    }

    private fun setRecipient(recipient: String?) {
        recipient?.let {
            txt_recipient.text = it
        }
    }

    private fun setViews(transaction: Transaction?) {

        /*when (transaction?.status) {
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
        }*/

//        txt_mobile_number?.text = it.mobileNumber
//        txt_message?.text = it.message

        /*if (!transaction.transactionQrCode.isNullOrEmpty()) {
            Glide.with(this).load(transaction.transactionQrCode)
                .thumbnail(1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image_qr)
        }*/
    }
}