package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.egpayawal.card_library.view.CreditCardExpiryTextWatcher
import com.uxi.bambupay.R
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.utils.Constants.Companion.CASH_IN_REDIRECT_URL
import com.uxi.bambupay.viewmodel.CashInViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_cash_in_card.*

/**
 * Created by Eraño Payawal on 12/15/20.
 * hunterxer31@gmail.com
 */
class CashInCardActivity : BaseActivity() {

    private val cashInViewModel by viewModels<CashInViewModel> { viewModelFactory }

    private val fromScreen by lazy {
        intent?.getStringExtra(Constants.SCREEN_FROM)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        initViews()
        observeViewModel()
        events()
    }

    override fun getLayoutId() = R.layout.activity_cash_in_card

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

        tv_toolbar_title?.text = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initViews() {
        text_input_card_expiration.addTextChangedListener(CreditCardExpiryTextWatcher(text_input_card_expiration))
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

    private fun viewNewClick() {
        text_input_amount.setText("")
        text_input_card_number.setText("")
        text_input_card_expiration.setText("")
        text_input_cvv.setText("")
        text_input_card_name.setText("")
    }

    private fun viewDashboardClick() {
        showMain()
    }

    private fun events() {
        btn_transact.setOnClickListener {
            // Deprecated
            // Business Logic and the API changes
            /*cashInViewModel.subscribeCashInPaynamics(
                text_input_amount.text.toString(),
                text_input_card_number.text.toString(),
                text_input_card_expiration.text.toString(),
                text_input_cvv.text.toString(),
                text_input_card_name.text.toString()
            )*/
            cashInViewModel.subscribeCashInPaynamics(text_input_amount.text.toString(), fromScreen)
        }

        btn_cancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showPaynamicsWebview(url: String) {
        val intent = Intent(this@CashInCardActivity, CashInPaynamicsActivity::class.java)
        intent.putExtra(Constants.AMOUNT, text_input_amount.text.toString())
        intent.putExtra(CASH_IN_REDIRECT_URL, url)
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

}