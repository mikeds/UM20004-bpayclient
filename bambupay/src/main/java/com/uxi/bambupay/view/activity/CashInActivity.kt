package com.uxi.bambupay.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.databinding.ActivityCashInBinding
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.view.ext.viewBinding
import com.uxi.bambupay.view.fragment.dialog.SuccessDialog
import com.uxi.bambupay.viewmodel.CashInViewModel
import com.uxi.bambupay.viewmodel.FeeViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel

class CashInActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val cashInViewModel by viewModels<CashInViewModel>{ viewModelFactory }
    private val feeViewModel by viewModels<FeeViewModel> { viewModelFactory }
    private val binding by viewBinding(ActivityCashInBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        initViews()
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
        setSupportActionBar(binding.appToolbar.toolbar)
        binding.appToolbar.tvToolbarTitle.text = getString(R.string.cash_in)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initViews() {
        // default
        binding.contentCashIn.textFeeMsg.text = getString(R.string.convenience_fee_msg, "0")
    }

    private fun events() {
        binding.contentCashIn.btnCancel.setOnClickListener {
            onBackPressed()
        }

        binding.contentCashIn.btnTransact.setOnClickListener {
            cashInViewModel.subscribeCashIn(binding.contentCashIn.textInputAmount.text.toString()/*, text_input_mobile.text.toString()*/)
        }

        binding.contentCashIn.textInputAmount.doAfterTextChanged {
            feeViewModel.subscribeFee(it.toString(), Constants.TX_TYPE_CASH_IN_OTC_ID)
        }
    }

    private fun observeViewModel() {
        userTokenModel.isTokenRefresh.observe(this, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
                cashInViewModel.subscribeCashIn(binding.contentCashIn.textInputAmount.text.toString()/*, text_input_mobile.text.toString()*/)
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

        cashInViewModel.isLoading.observe(this, Observer { isLoading ->
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
                binding.contentCashIn.textFee.text = fee
                binding.contentCashIn.textFeeMsg.text = getString(R.string.convenience_fee_msg, fee)
            }
        })

        cashInViewModel.cashInDataWithMessage.observe(this, Observer { it1 ->
            it1?.let {
                if (!it.first.isNullOrEmpty() && it.second != null) {
                    val amount = binding.contentCashIn.textInputAmount.text.toString()
                    val dialog = SuccessDialog(
                        ctx = this@CashInActivity,
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

        cashInViewModel.errorMessage.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                showMessageDialog(it)
            }
        })

    }

    private fun viewNewClick() {
        binding.contentCashIn.textInputAmount.setText("")
    }

    private fun viewDashboardClick() {
        showMain()
    }

}