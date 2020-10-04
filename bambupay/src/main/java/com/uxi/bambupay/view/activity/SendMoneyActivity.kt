package com.uxi.bambupay.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.view.fragment.dialog.SuccessDialog
import com.uxi.bambupay.viewmodel.TransactionViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_send_money.*

class SendMoneyActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val transactionViewModel by viewModel<TransactionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        observeViewModel()
        events()
    }

    override fun getLayoutId() = R.layout.activity_send_money

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        tv_toolbar_title?.text = getString(R.string.transact)
//        toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_more)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun events() {
        btn_cancel.setOnClickListener {
            onBackPressed()
        }

        btn_transact.setOnClickListener {
            transactionViewModel.subscribeSendMoney(text_input_amount.text.toString(), text_input_mobile.text.toString(), input_message.text.toString())
        }
    }

    private fun observeViewModel() {
        transactionViewModel.isAmountEmpty.observe(this, Observer { isAmountEmpty ->
            if (isAmountEmpty) {
                showDialogMessage("Amount Required")
            }
        })

        transactionViewModel.isRecipientEmpty.observe(this, Observer { isRecipientEmpty ->
            if (isRecipientEmpty) {
                showDialogMessage("Mobile Number Required")
            }
        })

        transactionViewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        transactionViewModel.errorMessage.observe(this, Observer { errorMessage ->
            showMessageDialog(errorMessage)
        })

        transactionViewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (!isSuccess) {
                // call token refresher
                userTokenModel.subscribeToken()
            }
        })

        transactionViewModel.sendMoneySuccessMsg.observe(this, Observer { message ->
            if (!message.isNullOrEmpty()) {
                val successDialog = SuccessDialog(this, message, text_input_amount.text.toString(), "Oct 03, 2020 | 10:00PM")
                successDialog.setOnSuccessDialogClickListener(object : SuccessDialog.OnSuccessDialogClickListener {
                    override fun onDashBoardClicked() {
                        finish()
                    }

                    override fun onNewClicked() {
                        text_input_amount.setText("")
                        text_input_mobile.setText("")
                        input_message.setText("")
                    }
                })
                successDialog.show()
            }
        })

        userTokenModel.isTokenRefresh.observe(this, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
                transactionViewModel.subscribeSendMoney(text_input_amount.text.toString(), text_input_mobile.text.toString(), input_message.text.toString())
            }
        })

    }

}