package com.uxi.bambupay.view.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import com.uxi.bambupay.R
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_verification.*

/**
 * Created by Eraño Payawal on 7/12/20.
 * hunterxer31@gmail.com
 */
class VerificationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
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
            Log.e("DEBUG", "verification send!")
        }
    }
}