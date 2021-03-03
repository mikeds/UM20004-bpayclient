package com.uxi.bambupay.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.uxi.bambupay.R
import com.uxi.bambupay.utils.Constants
import kotlinx.android.synthetic.main.activity_payqrcode.*
import kotlinx.android.synthetic.main.app_toolbar.*

/**
 * Created by Eraño Payawal on 10/4/20.
 * hunterxer31@gmail.com
 */
class SelectPayQRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payqrcode)
        setupToolbar()
        events()
    }

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
        tv_toolbar_title?.text = getString(R.string.pay_qr_code)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun events() {
        btn_scan_pay_qr.setOnClickListener {
            showScanPayQr()
        }

        btn_create_pay_qr.setOnClickListener {
            val intent = Intent(this@SelectPayQRActivity, OtpActivity::class.java)
//            val intent = Intent(this@SelectPayQRActivity, CreateQRActivity::class.java)
            intent.putExtra(Constants.SCREEN_FROM, Constants.CREATE_PAY_QR_SCREEN)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        btn_quick_pay_qr.setOnClickListener {
            val intent = Intent(this@SelectPayQRActivity, OtpActivity::class.java)
//            val intent = Intent(this@SelectPayQRActivity, QuickScanQRActivity::class.java)
            intent.putExtra(Constants.SCREEN_FROM, Constants.QUICK_PAY_SCAN_SCREEN)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

    }

    private fun showScanPayQr() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
//            val intent = Intent(this@PayQRActivity, OtpActivity::class.java)
            val intent = Intent(this@SelectPayQRActivity, ScanPayQrCodeActivity::class.java)
            intent.putExtra(Constants.SCREEN_FROM, Constants.SCAN_PAY_QR_SCREEN)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 10001
    }

}