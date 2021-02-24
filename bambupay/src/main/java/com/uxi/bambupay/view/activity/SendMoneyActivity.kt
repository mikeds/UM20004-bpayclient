package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.model.events.NewTransactionEvent
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.view.fragment.dialog.SuccessDialog
import com.uxi.bambupay.viewmodel.FeeViewModel
import com.uxi.bambupay.viewmodel.TransactionViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_send_money.*
import kotlinx.android.synthetic.main.content_send_money.btn_cancel
import kotlinx.android.synthetic.main.content_send_money.btn_transact
import kotlinx.android.synthetic.main.content_send_money.text_fee
import kotlinx.android.synthetic.main.content_send_money.text_input_amount
import kotlinx.android.synthetic.main.content_send_money.text_input_mobile
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SendMoneyActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val transactionViewModel by viewModel<TransactionViewModel>()
    private val feeViewModel by viewModels<FeeViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        observeViewModel()
        events()
        EventBus.getDefault().register(this)
    }

    override fun getLayoutId() = R.layout.activity_send_money

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
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
        text_convenience_fee.text = getString(R.string.convenience_fee_msg, "0")

        btn_cancel.setOnClickListener {
            onBackPressed()
        }

        btn_transact.setOnClickListener {
//            transactionViewModel.subscribeSendMoney(text_input_amount.text.toString(), text_input_mobile.text.toString(), input_message.text.toString())
            transactionViewModel.validation(text_input_amount.text.toString(), text_input_mobile.text.toString())
        }

        text_input_amount.doAfterTextChanged {
            feeViewModel.subscribeFee(it.toString(), Constants.TX_TYPE_TRANSFER_ID)
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
                val dialog = SuccessDialog(
                    ctx = this,
                    message = message,
                    amount = text_input_amount.text.toString(),
                    date = "Oct 03, 2020 | 10:00PM",
                    qrCodeUrl = null,
                    onNewClicked = ::viewNewClick,
                    onDashBoardClicked = ::viewDashboardClick
                )
                dialog.show()
            }
        })

        userTokenModel.isTokenRefresh.observe(this, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
                transactionViewModel.subscribeSendMoney(text_input_amount.text.toString(), text_input_mobile.text.toString(), input_message.text.toString())
            }
        })

        feeViewModel.fees.observe(this, Observer {
            it.getContentIfNotHandled()?.let { fee ->
                text_fee.text = fee
                text_convenience_fee.text = getString(R.string.convenience_fee_msg, fee)
            }
        })

        transactionViewModel.validationSuccess.observe(this, Observer {
            if (it) {
                showOtpScreen()
            }
        })
    }

    private fun showOtpScreen() {
        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra(Constants.SCREEN_FROM, Constants.SEND_MONEY_SCREEN)
        intent.putExtra(Constants.AMOUNT, text_input_amount.text.toString())
        intent.putExtra(Constants.RECIPIENT_NUMBER, text_input_mobile.text.toString())
        intent.putExtra(Constants.MESSAGE, input_message.text.toString())
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNewTransactionEvent(event: NewTransactionEvent) {
        viewNewClick()
    }

    private fun viewNewClick() {
        text_input_amount.setText("")
        text_input_mobile.setText("")
        input_message.setText("")
        text_fee.text = "0"
        text_convenience_fee.text = getString(R.string.convenience_fee_msg, "0")
    }

    private fun viewDashboardClick() {
        showMain()
    }

}