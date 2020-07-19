package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.viewmodel.LoginViewModel
import com.uxi.bambupay.viewmodel.VerifyViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_verification.*

/**
 * Created by Eraño Payawal on 7/12/20.
 * hunterxer31@gmail.com
 */
class VerificationActivity : BaseActivity() {

    private val viewModelLogin by viewModel<LoginViewModel>()
    private val verifyViewModel by viewModel<VerifyViewModel>()

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        username = intent?.getStringExtra("username")

        setupToolbar()
        observeViewModel()
        events()
    }

    override fun getLayoutId() = R.layout.activity_verification

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
        tv_toolbar_title?.text = getString(R.string.verification)
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
            verifyViewModel.subscribeVerifyCode(pin_view.text.toString(), username)
        }
    }

    private fun observeViewModel() {
        viewModelLogin.subscribeToken()

        viewModelLogin.refreshLogin.observe(this, Observer { isRefreshLogin ->
            if (isRefreshLogin) {
                Log.e("DEBUG", "isRefreshLogin")
                verifyViewModel.subscribeVerifyCode(pin_view.text.toString(), username)
            }
        })

        verifyViewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        verifyViewModel.isCodeEmpty.observe(this, Observer { isCodeEmpty ->
            if (isCodeEmpty) Toast.makeText(this, getString(R.string.signup_no_verify), Toast.LENGTH_SHORT).show()
        })

        verifyViewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                showLoginScreen()
            } else {
                viewModelLogin.subscribeToken()
            }
        })

        verifyViewModel.errorMessage.observe(this, Observer { errorMessage ->
            showMessageDialog(errorMessage)
        })

    }

    private fun showLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        finish()
    }
}