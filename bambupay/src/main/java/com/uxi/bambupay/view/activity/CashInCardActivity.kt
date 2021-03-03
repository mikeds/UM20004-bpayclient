package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.egpayawal.card_library.view.CreditCardExpiryTextWatcher
import com.uxi.bambupay.R
import com.uxi.bambupay.databinding.ActivityCashInCardBinding
import com.uxi.bambupay.model.events.NewTransactionEvent
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.utils.Constants.Companion.CASH_IN_REDIRECT_URL
import com.uxi.bambupay.view.ext.viewBinding
import com.uxi.bambupay.viewmodel.CashInViewModel
import com.uxi.bambupay.viewmodel.FeeViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Eraño Payawal on 12/15/20.
 * hunterxer31@gmail.com
 */
class CashInCardActivity : BaseActivity() {

    private val cashInViewModel by viewModels<CashInViewModel> { viewModelFactory }
    private val feeViewModel by viewModels<FeeViewModel> { viewModelFactory }
    private val binding by viewBinding(ActivityCashInCardBinding::inflate)

    private val fromScreen by lazy {
        intent?.getStringExtra(Constants.SCREEN_FROM)
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

    override fun getLayoutId() = R.layout.activity_cash_in_card

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
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
        val title = when (fromScreen) {
            Constants.CASH_IN_CARD_SCREEN -> {
                getString(R.string.cash_in_card)
            }
            Constants.CASH_IN_BANCNET_SCREEN -> {
                getString(R.string.cash_in_bancnet)
            }
            Constants.CASH_IN_GRAB_SCREEN -> {
                getString(R.string.cash_in_grab_pay)
            }
            Constants.CASH_IN_GCASH_SCREEN -> {
                getString(R.string.cash_in_gcash)
            }
            Constants.CASH_IN_PAYMAYA_SCREEN -> {
                getString(R.string.cash_in_paymaya)
            }
            else -> getString(R.string.cash_in_card)
        }

        binding.appToolbar.tvToolbarTitle.text = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initViews() {
        binding.contentCashInCard.textInputCardExpiration.addTextChangedListener(CreditCardExpiryTextWatcher(binding.contentCashInCard.textInputCardExpiration))

        val feeMsg = when (fromScreen) {
            Constants.CASH_IN_CARD_SCREEN -> {
                getString(R.string.cash_in_card_paymaya_fee_msg)
            }
            Constants.CASH_IN_BANCNET_SCREEN -> {
                getString(R.string.convenience_fee_msg, "0")
            }
            Constants.CASH_IN_GRAB_SCREEN -> {
                getString(R.string.cash_in_grab_fee_msg)
            }
            Constants.CASH_IN_GCASH_SCREEN -> {
                getString(R.string.cash_in_gcash_fee_msg)
            }
            Constants.CASH_IN_PAYMAYA_SCREEN -> {
                getString(R.string.cash_in_card_paymaya_fee_msg)
            }
            else -> getString(R.string.convenience_fee_msg, "0")
        }

        binding.contentCashInCard.textFeeMsg.text = feeMsg
    }

    private fun observeViewModel() {
        cashInViewModel.isLoading.observe(this, Observer {
            if (it) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        // Deprecated
        /*cashInViewModel.paynamicsDataWithMessage.observe(this, Observer { it1 ->
            it1?.let {
                if (!it.first.isNullOrEmpty() && it.second != null) {
                    val amount = text_input_amount.text.toString()
                    val dialog = SuccessDialog(
                        ctx = this@CashInCardActivity,
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
        })*/

        cashInViewModel.paynamicsData.observe(this, Observer { paynamics ->
            paynamics?.redirectUrl?.let {
                showPaynamicsWebview(it)
            }
        })

        cashInViewModel.errorMessage.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                showMessageDialog(it)
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNewTransactionEvent(event: NewTransactionEvent) {
        viewNewClick()
    }

    private fun viewNewClick() {
        binding.contentCashInCard.textInputAmount.setText("")
        binding.contentCashInCard.textInputCardNumber.setText("")
        binding.contentCashInCard.textInputCardExpiration.setText("")
        binding.contentCashInCard.textInputCvv.setText("")
        binding.contentCashInCard.textInputCardName.setText("")
    }

    private fun viewDashboardClick() {
        showMain()
    }

    private fun events() {
        binding.contentCashInCard.btnTransact.setOnClickListener {
            // Deprecated
            // Business Logic and the API changes
            /*cashInViewModel.subscribeCashInPaynamics(
                text_input_amount.text.toString(),
                text_input_card_number.text.toString(),
                text_input_card_expiration.text.toString(),
                text_input_cvv.text.toString(),
                text_input_card_name.text.toString()
            )*/
//            cashInViewModel.subscribeCashInPaynamics(binding.contentCashInCard.textInputAmount.text.toString(), fromScreen)

            val intent = Intent(this@CashInCardActivity, OtpActivity::class.java)
            intent.putExtra(Constants.SCREEN_FROM, fromScreen)
            intent.putExtra(Constants.AMOUNT, binding.contentCashInCard.textInputAmount.text.toString())
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        binding.contentCashInCard.btnCancel.setOnClickListener {
            onBackPressed()
        }

        if (!fromScreen.isNullOrEmpty() && fromScreen == Constants.CASH_IN_BANCNET_SCREEN) {
            binding.contentCashInCard.textInputAmount.doAfterTextChanged {
                feeViewModel.subscribeFee(it.toString(), Constants.TX_TYPE_CASH_IN_OTC_ID)
            }
        }
    }

    private fun showPaynamicsWebview(url: String) {
        val intent = Intent(this@CashInCardActivity, CashInPaynamicsActivity::class.java)
        intent.putExtra(Constants.AMOUNT, binding.contentCashInCard.textInputAmount.text.toString())
        intent.putExtra(CASH_IN_REDIRECT_URL, url)
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

}