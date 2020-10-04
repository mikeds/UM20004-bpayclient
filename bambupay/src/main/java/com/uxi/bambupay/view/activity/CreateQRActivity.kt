package com.uxi.bambupay.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.uxi.bambupay.R
import kotlinx.android.synthetic.main.activity_create_qrcode.*
import kotlinx.android.synthetic.main.app_toolbar.*

/**
 * Created by Eraño Payawal on 10/4/20.
 * hunterxer31@gmail.com
 */
class CreateQRActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        events()
    }

    override fun getLayoutId() = R.layout.activity_create_qrcode

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
        tv_toolbar_title?.text = getString(R.string.create_qr)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun events() {
        btn_retry.setOnClickListener {
            text_input_amount.setText("")
            image_view_qr_code.setImageDrawable(null)
            container_buttons.visibility = View.GONE
            btn_generate.visibility = View.VISIBLE
        }

        btn_cancel.setOnClickListener {
            onBackPressed()
        }

        btn_generate.setOnClickListener {
            btn_generate.visibility = View.GONE
            container_buttons.visibility = View.VISIBLE
        }
    }

}