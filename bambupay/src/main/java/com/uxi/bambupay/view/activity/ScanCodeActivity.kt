package com.uxi.bambupay.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.uxi.bambupay.R
import com.uxi.bambupay.view.widget.CircleOverlay
import com.uxi.bambupay.view.widget.QRDetector
import kotlinx.android.synthetic.main.activity_scan_code.*
import kotlinx.android.synthetic.main.app_toolbar.*


/**
 * Created by Eraño Payawal on 8/3/20.
 * hunterxer31@gmail.com
 */
class ScanCodeActivity : AppCompatActivity() {

    private var scannedQR: Boolean = false
    private var cameraSource: CameraSource? = null
    private var qrDetector: QRDetector? = null
    private var surfaceHolder: SurfaceHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_code)
        setupToolbar()
        initViews()
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
        tv_toolbar_title?.text = getString(R.string.scan_qr_code)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initViews() {
        textureCamera.holder.addCallback(callback)
    }

    private val callback = object : SurfaceHolder.Callback {
//        override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
//            surfaceHolder = p0
//        }
//
//        override fun surfaceDestroyed(p0: SurfaceHolder?) {
//            cameraSource?.stop()
//        }
//
//        override fun surfaceCreated(p0: SurfaceHolder?) {
//            surfaceHolder = p0
//            startBarcodeScanning()
//        }

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
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            cameraSource?.start(surfaceHolder)
        }
    }

    private fun startCamera() {
        if (ActivityCompat.checkSelfPermission(this@ScanCodeActivity, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED) {
            return
        }
        scannedQR = false
        startScanner()
    }

    private fun startScanner() {
        val barcodeDetector = BarcodeDetector.Builder(this@ScanCodeActivity)
//            .setBarcodeFormats(Barcode.QR_CODE or Barcode.DATA_MATRIX)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        qrDetector = QRDetector(barcodeDetector, CircleOverlay.DIAMETER, CircleOverlay.DIAMETER)
        qrDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(p0: Detector.Detections<Barcode>?) {
                if (p0?.detectedItems?.size()!! > 0 && !scannedQR) {
                    val displayValue = p0.detectedItems.valueAt(0).displayValue
                    Log.e("DEBUG", "displayValue ${displayValue.toString()}")
                    overlayView.post {
                        scannedQR = true
                        cameraSource?.stop()
                        val intent = Intent()
                        intent.putExtra(SCANNED_QR_CODE, displayValue)
                        setResult(SCAN_QR_CODE, intent)
                        finish()
//                        Log.e("DEBUG", "userId:: $userId")

                        /*if (userId != null) {
                            viewModel.shareBusinessCardByUser(userId)
                        } else {
                            AlertDialog.Builder(this@ScanCodeActivity, R.style.DialogStyle)
                                .setTitle(R.string.error)
                                .setMessage(R.string.error_transaction_number)
                                .setPositiveButton(android.R.string.ok, null)
                                .create()
                                .show()
                        }*/
                    }
                }
            }
        })

        cameraSource = CameraSource.Builder(this@ScanCodeActivity, qrDetector)
            .setRequestedPreviewSize(1600, 900)
            .setRequestedFps(15.0f)
            .setAutoFocusEnabled(true)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .build()
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 10001
        const val SCAN_QR_CODE = 200
        const val SCANNED_QR_CODE = "SCANNED_QR_CODE"
    }
}