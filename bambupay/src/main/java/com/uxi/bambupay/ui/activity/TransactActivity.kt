package com.uxi.bambupay.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.uxi.bambupay.R
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_transaction.*

class TransactActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        events()
    }

    override fun getLayoutId() = R.layout.activity_transact

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        tv_toolbar_title?.text = getString(R.string.transact)
//        toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_more)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun events() {
        btn_cancel.setOnClickListener {
            onBackPressed()
        }
    }
}