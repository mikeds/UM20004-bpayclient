package com.uxi.bambupay.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.model.events.NewTransactionEvent
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.viewmodel.FeeViewModel
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

    private val feeViewModel by viewModels<FeeViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        observeViewModel()
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

    private fun observeViewModel() {
        feeViewModel.fees.observe(this, Observer {
            it.getContentIfNotHandled()?.let { fee ->
                text_fee.text = fee
            }
        })
    }

    private fun  events() {
        btn_cancel.setOnClickListener {
            onBackPressed()
        }

        btn_scan_qr_code.setOnClickListener {
            showScanPayQr()
        }

        text_input_amount.doAfterTextChanged {
            feeViewModel.subscribeFee(it.toString(), Constants.TX_TYPE_QUICK_PAY_QR_ID)
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
            intent.putExtra(Constants.SCREEN_FROM, Constants.QUICK_PAY_SCAN_SCREEN)
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