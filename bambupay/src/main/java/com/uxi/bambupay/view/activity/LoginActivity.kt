package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    private val viewModelLogin by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Timber.tag("DEBUG").e("isLoggedIn:: ${utils?.isLoggedIn}")
        observeViewModel()
        events()
    }

    override fun getLayoutId() = R.layout.activity_login

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_sign_in -> {
                viewModelLogin.subscribeLogin(input_email.text.toString(), input_password.text.toString())
            }
            R.id.btn_register -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
            R.id.btn_forgot_password -> {
                val intent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
            R.id.btn_verification -> {
                val intent = Intent(this, VerificationEmailActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
        }
    }

    private fun observeViewModel() {
        viewModelLogin.subscribeToken()

        viewModelLogin.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        viewModelLogin.isSuccessLoggedIn.observe(this, Observer { isSuccessLoggedIn ->
            if (isSuccessLoggedIn) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                viewModelLogin.subscribeToken()
            }
        })

        viewModelLogin.refreshLogin.observe(this, Observer { isRefreshLogin ->
            if (isRefreshLogin) {
                Log.e("DEBUG", "isRefreshLogin")
                viewModelLogin.subscribeLogin(input_email.text.toString(), input_password.text.toString())
            }
        })

        viewModelLogin.errorMessage.observe(this, Observer { errorMessage ->
            Log.e("DEBUG", "login error")
            Toast.makeText(this, "$errorMessage", Toast.LENGTH_SHORT).show()
            showMessageDialog(errorMessage)
        })

    }

    private fun events() {
        btn_sign_in.setOnClickListener(this)
        btn_register.setOnClickListener(this)
        btn_forgot_password.setOnClickListener(this)
        btn_verification.setOnClickListener(this)
    }
}