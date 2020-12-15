package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.uxi.bambupay.R
import com.uxi.bambupay.utils.Constants
import kotlinx.android.synthetic.main.activity_select_cash_in.*
import kotlinx.android.synthetic.main.app_toolbar.*

/**
 * Created by Eraño Payawal on 8/14/20.
 * hunterxer31@gmail.com
 */
class SelectCashInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_cash_in)
        setupToolbar()
        events()
    }

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
        btn_cash_in_merchant.setOnClickListener {
            val intent = Intent(this@SelectCashInActivity, OtpActivity::class.java)
            intent.putExtra(Constants.SCREEN_FROM, Constants.CASH_IN_OTC_SCREEN)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        btn_cash_in_bank.setOnClickListener {
            val intent = Intent(this@SelectCashInActivity, SelectBankActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.stay)
        }

        btn_card.setOnClickListener {
            val intent = Intent(this@SelectCashInActivity, OtpActivity::class.java)
            intent.putExtra(Constants.SCREEN_FROM, Constants.CASH_IN_CARD_SCREEN)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }
    }

}