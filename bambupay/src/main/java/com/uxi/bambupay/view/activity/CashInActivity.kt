package com.uxi.bambupay.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.viewmodel.CashInViewModel
import com.uxi.bambupay.viewmodel.FeeViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_cash_in.*

class CashInActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val cashInViewModel by viewModel<CashInViewModel>()
    private val feeViewModel by viewModels<FeeViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        observeViewModel()
        events()
    }

    override fun getLayoutId() = R.layout.activity_cash_in

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
        tv_toolbar_title?.text = getString(R.string.cash_in)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun events() {
        btn_cancel.setOnClickListener {
            onBackPressed()
        }

        btn_transact.setOnClickListener {
            cashInViewModel.subscribeCashIn(text_input_amount.text.toString()/*, text_input_mobile.text.toString()*/)
        }

        text_input_amount.doAfterTextChanged {
            feeViewModel.subscribeFee(it.toString(), Constants.TX_TYPE_CASH_IN_OTC_ID)
        }
    }

    private fun observeViewModel() {
        userTokenModel.isTokenRefresh.observe(this, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
                cashInViewModel.subscribeCashIn(text_input_amount.text.toString()/*, text_input_mobile.text.toString()*/)
            }
        })

        cashInViewModel.isAmountEmpty.observe(this, Observer { isAmountEmpty ->
            if (isAmountEmpty) {
                showDialogMessage("Amount Required")
            }
        })

        cashInViewModel.isRecipientEmpty.observe(this, Observer { isRecipientEmpty ->
            if (isRecipientEmpty) {
                showDialogMessage("Mobile Number Required")
            }
        })

        cashInViewModel.isCashOutSuccess.observe(this, Observer { isCashOutSuccess ->
            if (isCashOutSuccess) {
//                finish()
                showMain()
            }
        })

        cashInViewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        cashInViewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (!isSuccess) {
                // call token refresher
                userTokenModel.subscribeToken()
            }
        })

        feeViewModel.fees.observe(this, Observer {
            it.getContentIfNotHandled()?.let { fee ->
                text_fee.text = fee
            }
        })

    }

}