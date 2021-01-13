package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.model.events.NewTransactionEvent
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.view.fragment.dialog.SuccessDialog
import com.uxi.bambupay.viewmodel.CashOutViewModel
import com.uxi.bambupay.viewmodel.OtpViewModel
import com.uxi.bambupay.viewmodel.QRCodeViewModel
import com.uxi.bambupay.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_otp.*
import kotlinx.android.synthetic.main.content_otp.btn_cancel
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

/**
 * Created by Eraño Payawal on 11/18/20.
 * hunterxer31@gmail.com
 */
class OtpActivity : BaseActivity() {

    private val otpViewModel by viewModels<OtpViewModel> { viewModelFactory }

    private val cashOutViewModel by viewModels<CashOutViewModel> { viewModelFactory }
    private val transactionViewModel by viewModels<TransactionViewModel> { viewModelFactory }
    private val qrCodeViewModel by viewModels<QRCodeViewModel> { viewModelFactory }

    private val fromScreen by lazy {
        intent?.getStringExtra(Constants.SCREEN_FROM)
    }

    private val mobileNumber by lazy {
        intent?.getStringExtra(Constants.MOBILE_NUMBER)
    }

    private val recipientNo by lazy {
        intent?.getStringExtra(Constants.RECIPIENT_NUMBER)
    }

    private val amount by lazy {
        intent?.getStringExtra(Constants.AMOUNT)
    }

    private val bankCode by lazy {
        intent?.getLongExtra(Constants.BANK_CODE, -0L)
    }

    private val accountNo by lazy {
        intent?.getStringExtra(Constants.ACCOUNT_NO)
    }

    private val message by lazy {
        intent?.getStringExtra(Constants.MESSAGE)
    }

    private val refIdNumber by lazy {
        intent?.getStringExtra(Constants.REF_ID_NUMBER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        events()
        observeViewModel()

        if (!fromScreen.isNullOrEmpty() && fromScreen == Constants.REGISTRATION_SCREEN) {
            otpViewModel.subscribeRequestOtp(mobileNum = mobileNumber, module = "reg")
        } else {
            otpViewModel.subscribeRequestOtp(mobileNum = mobileNumber)
        }
    }

    override fun getLayoutId() = R.layout.activity_otp

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
        tv_toolbar_title?.text = getString(R.string.otp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun events() {
        pin_view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val source = s.toString()
                val length = source.length
                if (length == 4) {
                    btn_send.alpha = 1f
                    btn_send.isEnabled = true
                } else {
                    btn_send.alpha = 0.5f
                    btn_send.isEnabled = false
                }
            }
        })

        btn_send.setOnClickListener {
            otpViewModel.subscribeSubmitOTP(pin_view.text.toString())
        }

        btn_cancel.setOnClickListener {
            onBackPressed()
        }

        btn_resend_otp.setOnClickListener {
            otpViewModel.subscribeRequestOtp(mobileNum = mobileNumber)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SUBSCRIBE_GLOBE_TOKEN) {
            val code = data?.getStringExtra("GLOBE_CODE")
            Timber.tag("DEBUG").e("globe token:: $code")
            otpViewModel.subscribeOtpCode(code)
        }
    }

    private fun observeViewModel() {
        otpViewModel.isLoading.observe(this, Observer {
            if (it) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })
        otpViewModel.redirectUrl.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                val intent = Intent(this@OtpActivity, WebviewOtpActivity::class.java)
                intent.putExtra(Constants.REDIRECT_URL, it)
                startActivityForResult(intent, SUBSCRIBE_GLOBE_TOKEN)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
        })
        otpViewModel.isSuccessOtp.observe(this, Observer {
            it.getContentIfNotHandled()?.let {  success ->
                if (success) {
                    screenIntent()
                }
            }
        })
        otpViewModel.errorMsg.observe(this, Observer {
            showDialogMessage(it)
        })

        // start cash out
        cashOutViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })
        cashOutViewModel.ubpCashOutDataWithMessage.observe(this, Observer { it1 ->
            it1?.let {
                if (!it.first.isNullOrEmpty() && it.second != null) {
                    val amount = this.amount
                    val dialog = SuccessDialog(
                        ctx = this@OtpActivity,
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
        cashOutViewModel.errorMessage.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                showMessageDialog(it)
            }
        })
        // end cash out

        // start send money
        transactionViewModel.sendMoneyDataWithMessage.observe(this, Observer { it1 ->
            it1?.let {
                if (!it.first.isNullOrEmpty() && it.second != null) {
                    val dialog = SuccessDialog(
                        ctx = this,
                        message = it.first,
                        amount = this.amount,
                        date = it.second?.timestamp,
                        qrCodeUrl = null,
                        onNewClicked = ::viewNewClick,
                        onDashBoardClicked = ::viewDashboardClick
                    )
                    dialog.show()
                }
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
        // end send money

        // start scanpayqr
        qrCodeViewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })
        qrCodeViewModel.scanPayQrDataWithMessage.observe(this, Observer { it1 ->
            it1?.let {
                if (!it.first.isNullOrEmpty() && it.second != null) {
                    val dialog = SuccessDialog(
                        ctx = this,
                        message = it.first,
                        amount = this.amount,
                        date = it.second?.timestamp,
                        qrCodeUrl = null,
                        onNewClicked = ::viewNewClick,
                        onDashBoardClicked = ::viewDashboardClick
                    )
                    dialog.show()
                }
            }
        })
        // end scanpayqr
    }

    private fun viewNewClick() {
        EventBus.getDefault().post(NewTransactionEvent())
        onBackPressed()
    }

    private fun viewDashboardClick() {
        showMain()
    }

    private fun screenIntent() {
        if (fromScreen.isNullOrEmpty()) return

        when (fromScreen) {

            Constants.CASH_IN_OTC_SCREEN -> {
                val intent = Intent(this@OtpActivity, CashInActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
            Constants.CASH_OUT_SCREEN -> {
                /*val intent = Intent(this@OtpActivity, SelectBankActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)*/
                cashOutViewModel.subscribeCashOut(amount, accountNo, bankCode)
            }
            Constants.SEND_MONEY_SCREEN -> {
                /*val intent = Intent(this@OtpActivity, SendMoneyActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)*/
                transactionViewModel.subscribeSendMoney(amount, recipientNo, message)
            }
            Constants.SCAN_PAY_QR_SCREEN -> {
                /*val intent = Intent(this@OtpActivity, ScanPayQrCodeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)*/
                qrCodeViewModel.subscribeScanPayQr(refIdNumber)
            }
            Constants.CREATE_PAY_QR_SCREEN -> {
                val intent = Intent(this@OtpActivity, CreateQRActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
            Constants.QUICK_PAY_SCAN_SCREEN -> {
                val intent = Intent(this@OtpActivity, QuickScanQRActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
            Constants.REGISTRATION_SCREEN -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                finish()
            }
            Constants.CASH_IN_CARD_SCREEN -> {
                val intent = Intent(this@OtpActivity, CashInCardActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }

        }
    }

    companion object {
        const val SUBSCRIBE_GLOBE_TOKEN = 0x0001
    }

}