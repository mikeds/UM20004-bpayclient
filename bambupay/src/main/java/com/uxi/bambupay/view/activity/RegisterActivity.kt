package com.uxi.bambupay.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.imageoptions.FullDocumentImageOptions
import com.microblink.uisettings.ActivityRunner
import com.microblink.uisettings.BlinkIdUISettings
import com.uxi.bambupay.R
import com.uxi.bambupay.view.widget.CreditCardExpiryTextWatcher
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_register.*

class RegisterActivity : BaseActivity() {

    companion object {
        const val MY_BLINKID_REQUEST_CODE = 123
    }

    private var recognizer: BlinkIdCombinedRecognizer? = null
    private var recognizerBundle: RecognizerBundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        events()
        setupMicroBlink()
    }

    override fun getLayoutId() = R.layout.activity_register

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // onActivityResult is called whenever we returned from activity started with startActivityForResult
        // We need to check request code to determine that we have really returned from BlinkID activity

        // onActivityResult is called whenever we returned from activity started with startActivityForResult
        // We need to check request code to determine that we have really returned from BlinkID activity
        if (requestCode != MY_BLINKID_REQUEST_CODE) {
            return
        }

        if (resultCode == Activity.RESULT_OK) {
            // OK result code means scan was successful
            if (data != null) {
                onScanSuccess(data)
            }
        } else {
            // user probably pressed Back button and cancelled scanning
            onScanCanceled()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun events() {
        //input_card_expiry.addTextChangedListener(CreditCardExpiryTextWatcher(input_card_expiry))
        btn_image_document.setOnClickListener {
            // use default UI for scanning documents
            val uiSettings = BlinkIdUISettings(recognizerBundle)
            // start scan activity based on UI settings
            ActivityRunner.startActivityForResult(this, MY_BLINKID_REQUEST_CODE, uiSettings)
        }

        btn_register.setOnClickListener {
            showVerificationScreen()
        }
    }

    private fun setupMicroBlink() {
        // we'll use Machine Readable Travel Document recognizer
        recognizer = BlinkIdCombinedRecognizer()

        val options = recognizer as FullDocumentImageOptions
        options.setReturnFullDocumentImage(true)

        // put our recognizer in bundle so that it can be sent via intent
        recognizerBundle = RecognizerBundle(recognizer)
    }

    private fun onScanSuccess(data: Intent) {
        // update recognizer results with scanned data
        recognizerBundle!!.loadFromIntent(data)

        // you can now extract any scanned data from result, we'll just get primary id
        val result = recognizer!!.result
        val fName = result.firstName
        val lName = result.lastName
        val fullName = result.fullName
        val gender = result.sex
        val dateOfBirth = result.dateOfBirth.originalDateString
        val imageFront = result.fullDocumentFrontImage
        val idNumber = result.documentNumber

        imageFront?.let {
            btn_image_document.setImageBitmap(it.convertToBitmap())
            txt_capture.visibility = View.GONE
        }

        if (!fName.isNullOrEmpty()) {
            input_fname.setText(fName)
        }

        if (!lName.isNullOrEmpty()) {
            input_lname.setText(lName)
        }

        if (input_fname.text.isNullOrEmpty() && input_lname.text.isNullOrEmpty() && !fullName.isNullOrEmpty()) {
            val strArr = fullName.split(",", "\\s+")
            val char1 = strArr[0]
            val char2 = strArr[1]
            if (!char1.isNullOrEmpty()) {
                input_lname.setText(char1)
            }
        }

        if (!dateOfBirth.isNullOrEmpty()) {
            input_date_of_birth.setText(dateOfBirth)
        }

        if (!gender.isNullOrEmpty()) {
            input_gender.setText(gender)
        }

        if (!idNumber.isNullOrEmpty()) {
            input_id_number.setText(idNumber)
        }

//        Toast.makeText(this, "Name: $name", Toast.LENGTH_LONG).show()
    }

    private fun onScanCanceled() {
        Toast.makeText(this, "Scan cancelled!", Toast.LENGTH_SHORT).show()
    }

    private fun showVerificationScreen() {
        val intent = Intent(this, VerificationActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

}