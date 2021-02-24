package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.databinding.ActivityCashOutBinding
import com.uxi.bambupay.model.events.NewTransactionEvent
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.view.ext.viewBinding
import com.uxi.bambupay.view.fragment.dialog.SuccessDialog
import com.uxi.bambupay.viewmodel.CashOutViewModel
import com.uxi.bambupay.viewmodel.FeeViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CashOutActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val cashOutViewModel by viewModel<CashOutViewModel>()
    private val feeViewModel by viewModels<FeeViewModel> { viewModelFactory }
    private val binding by viewBinding(ActivityCashOutBinding::inflate)

    private val bankCode by lazy {
        intent?.getLongExtra(Constants.BANK_CODE, -0L)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        initViews()
        observeViewModel()
        events()
        EventBus.getDefault().register(this)
    }

    override fun getLayoutId() = R.layout.activity_cash_out

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
        setSupportActionBar(binding.appToolbar.toolbar)
        binding.appToolbar.tvToolbarTitle.text = getString(R.string.cash_out)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initViews() {
        binding.contentCashOut.textFeeMsg.text = getString(R.string.convenience_fee_msg, "0")
    }

    private fun events() {
        binding.contentCashOut.btnCancel.setOnClickListener {
            onBackPressed()
        }

        binding.contentCashOut.btnTransact.setOnClickListener {
            // cashOutViewModel.subscribeCashOut(text_input_amount.text.toString(), text_input_account_no.text.toString(), bankCode)
            cashOutViewModel.validation(binding.contentCashOut.textInputAmount.text.toString(), binding.contentCashOut.textInputAccountNo.text.toString(), bankCode)
        }

        binding.contentCashOut.textInputAmount.doAfterTextChanged {
            feeViewModel.subscribeFee(it.toString(), Constants.TX_TYPE_CASH_OUT_OTC_ID)
        }
    }

    private fun showOtpScreen() {
        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra(Constants.SCREEN_FROM, Constants.CASH_OUT_SCREEN)
        intent.putExtra(Constants.AMOUNT, binding.contentCashOut.textInputAmount.text.toString())
        intent.putExtra(Constants.ACCOUNT_NO, binding.contentCashOut.textInputAccountNo.text.toString())
        intent.putExtra(Constants.BANK_CODE, bankCode)
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

    private fun observeViewModel() {
        userTokenModel.isTokenRefresh.observe(this, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
//                cashOutViewModel.subscribeCashOut(text_input_amount.text.toString(), text_input_mobile.text.toString())
                cashOutViewModel.subscribeCashOut(binding.contentCashOut.textInputAmount.text.toString(), binding.contentCashOut.textInputAccountNo.text.toString(), bankCode)
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
                binding.contentCashOut.textFee.text = fee
                binding.contentCashOut.textFeeMsg.text = getString(R.string.convenience_fee_msg, fee)
            }
        })

        cashOutViewModel.ubpCashOutDataWithMessage.observe(this, Observer { it1 ->
            it1?.let {
                if (!it.first.isNullOrEmpty() && it.second != null) {
                    val amount = binding.contentCashOut.textInputAmount.text.toString()
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

        cashOutViewModel.errorMessage.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                showMessageDialog(it)
            }
        })

        cashOutViewModel.validationSuccess.observe(this, Observer {
            if (it) {
                showOtpScreen()
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNewTransactionEvent(event: NewTransactionEvent) {
        viewNewClick()
    }

    private fun viewNewClick() {
        binding.contentCashOut.textInputAccountNo.setText("")
        binding.contentCashOut.textInputAmount.setText("")
        binding.contentCashOut.textFee.text = "0"
        binding.contentCashOut.textFeeMsg.text = getString(R.string.convenience_fee_msg, "0")
    }

    private fun viewDashboardClick() {
        showMain()
    }

}