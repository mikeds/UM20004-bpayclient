package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.uxi.bambupay.R
import com.uxi.bambupay.utils.Constants
import kotlinx.android.synthetic.main.activity_verification_mobile.*
import kotlinx.android.synthetic.main.activity_verification_mobile.input_mobile_number
import kotlinx.android.synthetic.main.app_toolbar.*
import timber.log.Timber

/**
 * Created by Eraño Payawal on 12/14/20.
 * hunterxer31@gmail.com
 */
class VerificationMobileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        observeViewModel()
        events()
    }

    override fun getLayoutId() = R.layout.activity_verification_mobile

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
        tv_toolbar_title?.text = getString(R.string.mobile_verification)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun observeViewModel() {

    }

    private fun events() {
        btn_send.setOnClickListener {
            val mobileNum = input_mobile_number.text.toString()
            if (mobileNum.isEmpty()) {
                showMessageDialog(getString(R.string.signup_no_mobile))
            } else {
                showOtpScreen(mobileNum)
            }
        }
    }

    private fun showOtpScreen(mobile: String) {
        Timber.tag("DEBUG").e("showOtpScreen mobile:: $mobile")
        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra(Constants.SCREEN_FROM, Constants.REGISTRATION_SCREEN)
        intent.putExtra(Constants.MOBILE_NUMBER, mobile)
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }
}