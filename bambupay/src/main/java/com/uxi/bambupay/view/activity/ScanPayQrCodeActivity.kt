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
import com.uxi.bambupay.databinding.ActivityScanPayQrCodeBinding
import com.uxi.bambupay.model.events.NewTransactionEvent
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.view.ext.viewBinding
import com.uxi.bambupay.view.fragment.dialog.SuccessDialog
import com.uxi.bambupay.viewmodel.QRCodeViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Eraño Payawal on 10/12/20.
 * hunterxer31@gmail.com
 */
class ScanPayQrCodeActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val qrCodeViewModel by viewModel<QRCodeViewModel>()

    private val binding by viewBinding(ActivityScanPayQrCodeBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        events()
        observeViewModel()
        EventBus.getDefault().register(this)
    }

    override fun getLayoutId() = R.layout.activity_scan_pay_qr_code

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanCodeActivity.SCAN_QR_CODE) {
            val qrCode: String? = data?.getStringExtra(ScanCodeActivity.SCANNED_QR_CODE)
            Log.e("DEBUG", "qrCode:: $qrCode")
            qrCode?.let {
                binding.contentScanPayQrCode.textInputRefNum.setText(it)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.appToolbar.toolbar)
        binding.appToolbar.tvToolbarTitle.text = getString(R.string.scan_pay)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun events() {
        binding.contentScanPayQrCode.btnScanQrCode.setOnClickListener {
            cameraPermission()
        }
        binding.contentScanPayQrCode.btnCancel.setOnClickListener {
            onBackPressed()
        }
        binding.contentScanPayQrCode.btnTransact.setOnClickListener {
//            qrCodeViewModel.subscribeScanPayQr(text_input_ref_num.text.toString())
            qrCodeViewModel.validation(binding.contentScanPayQrCode.textInputRefNum.text.toString())
        }
    }

    private fun showOtpScreen() {
        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra(Constants.SCREEN_FROM, Constants.SCAN_PAY_QR_SCREEN)
        intent.putExtra(Constants.REF_ID_NUMBER, binding.contentScanPayQrCode.textInputRefNum.text.toString())
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
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
                qrCodeViewModel.subscribeScanPayQr(binding.contentScanPayQrCode.textInputRefNum.text.toString())
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

        qrCodeViewModel.validationSuccess.observe(this, Observer {
            if (it) {
                // on-hold
                /*val builder = AlertDialog.Builder(this)
                builder.setMessage(getString(R.string.alert_msg))
                builder.setPositiveButton(getString(R.string.action_okay)) { dialogInt: DialogInterface, _: Int ->
                    showOtpScreen()
                    dialogInt.dismiss()
                }
                builder.setNegativeButton(getString(R.string.action_cancel)) { dialogInt: DialogInterface, _: Int ->
                    dialogInt.dismiss()
                }
                builder.create().show()*/
                showOtpScreen()
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNewTransactionEvent(event: NewTransactionEvent) {
        viewNewClick()
    }

    private fun viewNewClick() {
        binding.contentScanPayQrCode.textInputRefNum.setText("")
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