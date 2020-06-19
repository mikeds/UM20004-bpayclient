package com.uxi.bambupay.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.uxi.bambupay.MainActivity
import com.uxi.bambupay.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        events()
    }

    override fun getLayoutId() = R.layout.activity_login

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_sign_in -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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
        }
    }

    private fun events() {
        btn_sign_in.setOnClickListener(this)
        btn_register.setOnClickListener(this)
        btn_forgot_password.setOnClickListener(this)
    }
}