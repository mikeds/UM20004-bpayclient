package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.viewmodel.LoginViewModel
import com.uxi.bambupay.viewmodel.VerifyViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_forgot_password.*

class VerificationEmailActivity : BaseActivity() {

    private val verifyViewModel by viewModel<VerifyViewModel>()
    private val viewModelLogin by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        observeViewModel()
        events()
    }

    override fun getLayoutId() = R.layout.activity_forgot_password

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
        tv_toolbar_title?.text = getString(R.string.email_verification)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun observeViewModel() {
        viewModelLogin.subscribeToken()

        verifyViewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        verifyViewModel.isEmailEmpty.observe(this, Observer { isEmailEmpty ->
            if (isEmailEmpty) input_email.error = getString(R.string.signup_no_email)
        })

        verifyViewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                showVerificationScreen()
            } else {
                viewModelLogin.subscribeToken()
            }
        })

        viewModelLogin.refreshLogin.observe(this, Observer { isRefreshLogin ->
            if (isRefreshLogin) {
                Log.e("DEBUG", "isRefreshLogin")
                verifyViewModel.subscribeResendVerification(input_email.text.toString())
            }
        })

        verifyViewModel.errorMessage.observe(this, Observer { errorMessage ->
            showMessageDialog(errorMessage)
        })

    }

    private fun events() {
        btn_send.setOnClickListener {
            verifyViewModel.subscribeResendVerification(input_email.text.toString())
        }
    }

    private fun showVerificationScreen() {
        val intent = Intent(this, VerificationActivity::class.java)
        intent.putExtra("username", input_email.text.toString())
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }
}