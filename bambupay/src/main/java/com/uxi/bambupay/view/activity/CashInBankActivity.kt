package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.viewmodel.InstaPayViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_cash_in_bank.*

/**
 * Created by Eraño Payawal on 8/24/20.
 * hunterxer31@gmail.com
 */
class CashInBankActivity : BaseActivity() {

    private val instaPayViewModel by viewModel<InstaPayViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        observeViewModel()
        events()
    }

    override fun getLayoutId() = R.layout.activity_cash_in_bank

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

    private var accessToken: String? = null
    private fun observeViewModel() {
        instaPayViewModel.accessToken.observe(this, Observer { accessToken ->
            if (!accessToken.isNullOrEmpty()) {
                this.accessToken = accessToken
                instaPayViewModel.subscribeUbpSingleTransfer(
                    text_input_account_name.text.toString(),
                    text_input_account_no.text.toString(),
                    "",
                    text_input_amount.text.toString(),
                    accessToken
                )
            }
        })

        instaPayViewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        instaPayViewModel.successTransfer.observe(this, Observer { successTransfer ->
            if (successTransfer) {
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()

                val handler = Handler()
                handler.postDelayed(Runnable {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }, 500)
            }
        })
    }

    private fun events() {
        btn_transact.setOnClickListener {
            if (accessToken.isNullOrEmpty()) {
                instaPayViewModel.subscribePartnerToken()
            } else {
                instaPayViewModel.subscribeUbpSingleTransfer(
                    text_input_account_name.text.toString(),
                    text_input_account_no.text.toString(),
                    "",
                    text_input_amount.text.toString(),
                    accessToken!!
                )
            }
        }
    }
}