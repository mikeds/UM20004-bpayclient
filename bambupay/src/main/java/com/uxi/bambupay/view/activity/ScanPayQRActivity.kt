package com.uxi.bambupay.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.SurfaceHolder
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.uxi.bambupay.R
import com.uxi.bambupay.model.events.NewTransactionEvent
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.view.fragment.dialog.SuccessDialog
import com.uxi.bambupay.view.widget.CircleOverlay
import com.uxi.bambupay.view.widget.QRDetector
import com.uxi.bambupay.viewmodel.QRCodeViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.activity_scan_qr_code.*
import kotlinx.android.synthetic.main.app_toolbar.*
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

/**
 * Created by Eraño Payawal on 10/4/20.
 * hunterxer31@gmail.com
 */
class ScanPayQRActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val qrCodeViewModel by viewModel<QRCodeViewModel>()

    private var scannedQR: Boolean = false
    private var cameraSource: CameraSource? = null
    private var qrDetector: QRDetector? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var currScanValue: String? = null

    private val fromScreen by lazy {
        intent?.extras?.getString(Constants.SCREEN_FROM)
    }

    private val amount by lazy {
        intent?.extras?.getString(Constants.AMOUNT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        initViews()
        events()
        observeViewModel()
    }

    override fun getLayoutId() = R.layout.activity_scan_qr_code

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
        tv_toolbar_title?.text = getString(R.string.scan_pay)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initViews() {
        textureCamera.holder.addCallback(callback)
    }

    private fun events() {
        btn_cancel.setOnClickListener {
            onBackPressed()
        }

        btn_retry.setOnClickListener {
            startBarcodeScanning()
            container_buttons.visibility = View.GONE
        }
    }

    private fun observeViewModel() {
        userTokenModel.isTokenRefresh.observe(this, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
                if (fromScreen.isNullOrEmpty()) {
                    // scan to pay
                } else {
                    // quick scan to pay
                    qrCodeViewModel.subscriptionQuickPay(currScanValue, amount)
                }
            }
        })

        qrCodeViewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (!isSuccess) {
                // call token refresher
                userTokenModel.subscribeToken()
            }
        })

        qrCodeViewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        qrCodeViewModel.errorMessage.observe(this, Observer { errorMessage ->
            showMessageDialog(errorMessage)
        })

        var message = ""
        qrCodeViewModel.quickPaySuccessMsg.observe(this, Observer { successMessage ->
            message = successMessage
        })
        qrCodeViewModel.quickPayData.observe(this, Observer {
            val successDialog = SuccessDialog(this, message, amount, "Oct 03, 2020 | 10:00PM", it.qrCode)
            successDialog.setOnSuccessDialogClickListener(object : SuccessDialog.OnSuccessDialogClickListener {
                override fun onDashBoardClicked() {
                    showMain()
                }

                override fun onNewClicked() {
                    EventBus.getDefault().post(NewTransactionEvent())
                    finish()
                }
            })
            successDialog.show()
        })

    }

    private val callback = object : SurfaceHolder.Callback {

        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
            surfaceHolder = p0
        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            cameraSource?.stop()
        }

        override fun surfaceCreated(p0: SurfaceHolder) {
            surfaceHolder = p0
            startBarcodeScanning()
        }
    }

    private fun startBarcodeScanning() {
        startCamera()
        if (surfaceHolder != null) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            cameraSource?.start(surfaceHolder)
        }
    }

    private fun startCamera() {
        if (ActivityCompat.checkSelfPermission(this@ScanPayQRActivity, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED) {
            return
        }
        scannedQR = false
        startScanner()
    }

    private fun startScanner() {
        val barcodeDetector = BarcodeDetector.Builder(this@ScanPayQRActivity)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        qrDetector = QRDetector(barcodeDetector, CircleOverlay.DIAMETER, CircleOverlay.DIAMETER)
        qrDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(p0: Detector.Detections<Barcode>?) {
                if (p0?.detectedItems?.size()!! > 0 && !scannedQR) {
                    val displayValue = p0.detectedItems.valueAt(0).displayValue
                    currScanValue = displayValue
                    Timber.tag("DEBUG").e("displayValue:: $displayValue")
                    Timber.tag("DEBUG").e("fromScreen:: $fromScreen")
                    overlayView.post {
                        scannedQR = true
                        cameraSource?.stop()
                        container_buttons.visibility = View.VISIBLE

                        if (fromScreen.isNullOrEmpty()) {
                            // scan to pay
                        } else {
                            // quick scan to pay
                            qrCodeViewModel.subscriptionQuickPay(displayValue, amount)
                        }
                    }
                }
            }
        })

        cameraSource = CameraSource.Builder(this@ScanPayQRActivity, qrDetector)
            .setRequestedPreviewSize(1600, 900)
            .setRequestedFps(15.0f)
            .setAutoFocusEnabled(true)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .build()
    }

}