package com.uxi.bambupay.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.view.fragment.dialog.SuccessDialog
import com.uxi.bambupay.viewmodel.CashOutViewModel
import com.uxi.bambupay.viewmodel.FeeViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_cash_out.*
import kotlinx.android.synthetic.main.content_cash_out.btn_cancel
import kotlinx.android.synthetic.main.content_cash_out.btn_transact
import kotlinx.android.synthetic.main.content_cash_out.text_fee
import kotlinx.android.synthetic.main.content_cash_out.text_input_amount

class CashOutActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val cashOutViewModel by viewModel<CashOutViewModel>()
    private val feeViewModel by viewModels<FeeViewModel> { viewModelFactory }

    private val bankCode by lazy {
        intent?.getLongExtra("bank_code", -0L)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        observeViewModel()
        events()
    }

    override fun getLayoutId() = R.layout.activity_cash_out

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
        tv_toolbar_title?.text = getString(R.string.cash_out)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun events() {
        btn_cancel.setOnClickListener {
            onBackPressed()
        }

        btn_transact.setOnClickListener {
            cashOutViewModel.subscribeCashOut(text_input_amount.text.toString(), text_input_account_no.text.toString(), bankCode)
        }

        text_input_amount.doAfterTextChanged {
            feeViewModel.subscribeFee(it.toString(), Constants.TX_TYPE_CASH_OUT_OTC_ID)
        }
    }

    private fun observeViewModel() {
        userTokenModel.isTokenRefresh.observe(this, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
//                cashOutViewModel.subscribeCashOut(text_input_amount.text.toString(), text_input_mobile.text.toString())
                cashOutViewModel.subscribeCashOut(text_input_amount.text.toString(), text_input_account_no.text.toString(), bankCode)
            }
        })

        cashOutViewModel.isAmountEmpty.observe(this, Observer { isAmountEmpty ->
            if (isAmountEmpty) {
                showDialogMessage("Amount Required")
            }
        })

        cashOutViewModel.isRecipientEmpty.observe(this, Observer { isRecipientEmpty ->
            if (isRecipientEmpty) {
                showDialogMessage("Mobile Number Required")
            }
        })

        cashOutViewModel.isCashOutSuccess.observe(this, Observer { isCashOutSuccess ->
            if (isCashOutSuccess) {
//                finish()
                showMain()
            }
        })

        cashOutViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        cashOutViewModel.isSuccess.observe(this, Observer { isSuccess ->
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

        cashOutViewModel.ubpCashOutDataWithMessage.observe(this, Observer { it1 ->
            it1?.let {
                if (!it.first.isNullOrEmpty() && it.second != null) {
                    val amount = text_input_amount.text.toString()
                    val dialog = SuccessDialog(
                        ctx = this@CashOutActivity,
                        message = it.first,
                        amount = amount,
                        date = it.second?.timestamp,
                        qrCodeUrl = it.second?.qrCode,
                        onNewClicked = ::viewNewClick,
                        onDashBoardClicked = ::viewDashboardClick
                    )
                    dialog.show()
                }
            }
        })

    }

    private fun viewNewClick() {
        text_input_account_no.setText("")
        text_input_amount.setText("")
        text_fee.text = "0"
    }

    private fun viewDashboardClick() {
        showMain()
    }

}