package com.uxi.bambupay.ui.activity

import android.content.Intent
import android.os.Bundle
import com.uxi.bambupay.MainActivity
import com.uxi.bambupay.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_sign_in.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }
    }

    override fun getLayoutId() = R.layout.activity_login
}