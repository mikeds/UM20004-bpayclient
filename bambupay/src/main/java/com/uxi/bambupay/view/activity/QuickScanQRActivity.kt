package com.uxi.bambupay.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.uxi.bambupay.R
import com.uxi.bambupay.model.events.NewTransactionEvent
import com.uxi.bambupay.utils.Constants
import kotlinx.android.synthetic.main.activity_quick_scan_qr.*
import kotlinx.android.synthetic.main.app_toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Eraño Payawal on 10/5/20.
 * hunterxer31@gmail.com
 */
class QuickScanQRActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        events()
        EventBus.getDefault().register(this)
    }

    override fun getLayoutId() = R.layout.activity_quick_scan_qr

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
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
        tv_toolbar_title?.text = getString(R.string.quick_qr)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun  events() {
        btn_cancel.setOnClickListener {
            onBackPressed()
        }

        btn_scan_qr_code.setOnClickListener {
            showScanPayQr()
        }
    }

    private fun showScanPayQr() {

        if (text_input_amount.text.toString().isEmpty()) {
            alertMessage(getString(R.string.amount_required))
            return
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), PayQRActivity.CAMERA_REQUEST_CODE
            )
        } else {
            val intent = Intent(this@QuickScanQRActivity, ScanPayQRActivity::class.java)
            intent.putExtra(Constants.SCREEN_FROM, Constants.QUICK_SCAN_SCREEN)
            intent.putExtra(Constants.AMOUNT, text_input_amount.text.toString())
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }
    }

    private fun alertMessage(message: String) {
        AlertDialog.Builder(this@QuickScanQRActivity, R.style.DialogStyle)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { view, _ -> view.dismiss() }
            .create()
            .show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNewTransactionEvent(event: NewTransactionEvent) {
        text_input_amount.setText("")
    }

}