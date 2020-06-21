package com.uxi.bambupay

import android.app.Application
import com.microblink.MicroblinkSDK
import com.microblink.intent.IntentDataTransferMode

class BambuPayApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Micro-Blink ID
        MicroblinkSDK.setLicenseKey(getString(R.string.demo_microblink_key), this)
        // use optimised way for transferring RecognizerBundle between activities, while ensuring
        // data does not get lost when Android restarts the scanning activity
        MicroblinkSDK.setIntentDataTransferMode(IntentDataTransferMode.PERSISTED_OPTIMISED)
    }
}