package com.uxi.bambupay.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.uxi.bambupay.R
import com.uxi.bambupay.view.fragment.dialog.SuccessDialog
import com.uxi.bambupay.viewmodel.QRCodeViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_scan_pay_qr_code.*
import kotlinx.android.synthetic.main.content_scan_pay_qr_code.btn_cancel
import kotlinx.android.synthetic.main.content_scan_pay_qr_code.btn_transact

/**
 * Created by Eraño Payawal on 10/12/20.
 * hunterxer31@gmail.com
 */
class ScanPayQrCodeActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val qrCodeViewModel by viewModel<QRCodeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        events()
        observeViewModel()
    }

    override fun getLayoutId() = R.layout.activity_scan_pay_qr_code

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanCodeActivity.SCAN_QR_CODE) {
            val qrCode: String? = data?.getStringExtra(ScanCodeActivity.SCANNED_QR_CODE)
            Log.e("DEBUG", "qrCode:: $qrCode")
            qrCode?.let {
                text_input_ref_num.setText(it)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        tv_toolbar_title?.text = getString(R.string.scan_pay)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun events() {
        btn_scan_qr_code.setOnClickListener {
            cameraPermission()
        }
        btn_cancel.setOnClickListener {
            onBackPressed()
        }
        btn_transact.setOnClickListener {
            qrCodeViewModel.subscribeScanPayQr(text_input_ref_num.text.toString())
        }
    }

    private fun observeViewModel() {
        qrCodeViewModel.isTransactionNumberEmpty.observe(this, { isTransactionNumberEmpty ->
            if (isTransactionNumberEmpty) {
                showDialogMessage("Transaction Number Required")
            }
        })

        qrCodeViewModel.successMessage.observe(this, { successMessage ->
            if (!successMessage.isNullOrEmpty()) {
//                alertMessage(successMessage)
            }
        })

        qrCodeViewModel.errorMessage.observe(this, { failedMessage ->
            if (!failedMessage.isNullOrEmpty()) {
                showDialogMessage(failedMessage)
            }
        })

        qrCodeViewModel.loading.observe(this, { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        qrCodeViewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (!isSuccess) {
                // call token refresher
                userTokenModel.subscribeToken()
            }
        })

        userTokenModel.isTokenRefresh.observe(this, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
                qrCodeViewModel.subscribeScanPayQr(text_input_ref_num.text.toString())
            }
        })

        var message = ""
        qrCodeViewModel.quickPaySuccessMsg.observe(this, { successMessage ->
            message = successMessage
        })
        qrCodeViewModel.quickPayData.observe(this, {
            val dialog = SuccessDialog(
                ctx = this,
                message = message,
                amount = "",
                date = "Oct 03, 2020 | 10:00PM",
                qrCodeUrl = it.qrCode,
                onNewClicked = ::viewNewClick,
                onDashBoardClicked = ::viewDashboardClick
            )
            dialog.show()
        })
    }

    private fun viewNewClick() {
        text_input_ref_num.setText("")
    }

    private fun viewDashboardClick() {
        showMain()
    }

    private fun cameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), ScanCodeActivity.CAMERA_REQUEST_CODE
            )
        } else {
            showScanCode()
        }
    }

    private fun showScanCode() {
        val intent = Intent(this@ScanPayQrCodeActivity, ScanCodeActivity::class.java)
        startActivityForResult(intent, ScanCodeActivity.SCAN_QR_CODE)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }
}