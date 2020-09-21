package com.uxi.bambupay.view.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.microblink.entities.recognizers.RecognizerBundle
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer
import com.microblink.entities.recognizers.blinkid.imageoptions.FullDocumentImageOptions
import com.uxi.bambupay.R
import com.uxi.bambupay.model.Province
import com.uxi.bambupay.view.adapter.ProvinceAdapter
import com.uxi.bambupay.viewmodel.LoginViewModel
import com.uxi.bambupay.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_register.*
import timber.log.Timber
import java.text.DateFormat
import java.util.*


class RegisterActivity : BaseActivity(), View.OnClickListener {

    private val registerViewModel by viewModel<RegisterViewModel>()
    private val viewModelLogin by viewModel<LoginViewModel>()

    companion object {
        const val MY_BLINKID_REQUEST_CODE = 123
    }

    private var recognizer: BlinkIdCombinedRecognizer? = null
    private var recognizerBundle: RecognizerBundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        iniSpinner()
        observeViewModel()
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                registerViewModel.subscribeRegister(
                    input_fname.text.toString(),
                    input_lname.text.toString(),
                    input_gender.text.toString(),
                    input_date_of_birth.text.toString(),
                    input_id_number.text.toString(),
                    input_mobile_number.text.toString(),
                    input_email.text.toString(),
                    input_password.text.toString(),
                    input_confirm_password.text.toString(),
                    input_policy.isChecked
                )
            }
            R.id.input_gender -> {
                showGenderDialog()
            }
            R.id.input_date_of_birth -> {
                showDatePicker()
            }
        }
    }

    private fun events() {
        //input_card_expiry.addTextChangedListener(CreditCardExpiryTextWatcher(input_card_expiry))
        btn_image_document.setOnClickListener {
//            // use default UI for scanning documents
//            val uiSettings = BlinkIdUISettings(recognizerBundle)
//            // start scan activity based on UI settings
//            ActivityRunner.startActivityForResult(this, MY_BLINKID_REQUEST_CODE, uiSettings)

            // using BottomSheetDialog
            val dialogView: View = layoutInflater.inflate(R.layout.bottom_sheet, null)
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(dialogView)
            val btnTakePhoto = dialogView.findViewById<TextView>(R.id.btn_take_photo)
            val btnChooseGallery = dialogView.findViewById<TextView>(R.id.btn_choose_gallery)
            btnTakePhoto?.setOnClickListener {
                dialog.dismiss()
            }
            btnChooseGallery?.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()

        }

        btn_register.setOnClickListener(this)
        input_gender.setOnClickListener(this)
        input_date_of_birth.setOnClickListener(this)
//            showVerificationScreen()
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

    }

    private fun onScanCanceled() {
        Toast.makeText(this, "Scan cancelled!", Toast.LENGTH_SHORT).show()
    }

    private fun showVerificationScreen() {
        val intent = Intent(this, VerificationActivity::class.java)
        intent.putExtra("username", input_email.text.toString())
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

    private fun showGenderDialog() {
        val singleChoiceItems = resources.getStringArray(R.array.gender_choice_array)
        val itemSelected = 0
        var currSelectedIndex = 0

        val builder = AlertDialog.Builder(this)
            .setTitle("Select your gender")
            .setSingleChoiceItems(singleChoiceItems, itemSelected) { _, selectedIndex ->
                currSelectedIndex = selectedIndex
            }
            .setPositiveButton("Okay") { dialog, _ ->
                input_gender.setText(singleChoiceItems[currSelectedIndex])
                input_gender.error = null
                dialog.cancel()
            }
            .setNegativeButton("Cancel", null)

        builder.create().show()
    }

    private fun showDatePicker() {
        val mCalendar: Calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this, R.style.DatePickerDialogStyle,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                mCalendar.set(Calendar.YEAR, year)
                mCalendar.set(Calendar.MONTH, monthOfYear)
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date: String =
                    DateFormat.getDateInstance(DateFormat.MEDIUM).format(mCalendar.time)
                input_date_of_birth.setText(date)
                input_date_of_birth.error = null
            },
            mCalendar.get(Calendar.YEAR),
            mCalendar.get(Calendar.MONTH),
            mCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setTitle("Select Date")
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }

    private fun observeViewModel() {
        viewModelLogin.subscribeToken()
        registerViewModel.subscribeProvince()

        registerViewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        registerViewModel.isFirstNameEmpty.observe(this, Observer { isFirstNameEmpty ->
            if (isFirstNameEmpty) input_fname.error = getString(R.string.signup_no_firstname)
            nested_scroll_view.requestChildFocus(input_fname, input_fname)
        })

        registerViewModel.isLastNameEmpty.observe(this, Observer { isLastNameEmpty ->
            if (isLastNameEmpty) input_lname.error = getString(R.string.signup_no_lastname)
            nested_scroll_view.requestChildFocus(input_lname, input_lname)
        })

        /*registerViewModel.isGenderEmpty.observe(this, Observer { isGenderEmpty ->
            if (isGenderEmpty) input_gender.error = getString(R.string.signup_no_gender)
            nested_scroll_view.requestChildFocus(input_gender, input_gender)
        })

        registerViewModel.isDobEmpty.observe(this, Observer { isDobEmpty ->
            if (isDobEmpty) input_date_of_birth.error = getString(R.string.signup_no_dob)
            nested_scroll_view.requestChildFocus(input_date_of_birth, input_date_of_birth)
        })*/

        registerViewModel.isIdNumberEmpty.observe(this, Observer { isIdNumberEmpty ->
            if (isIdNumberEmpty) input_id_number.error = getString(R.string.signup_no_id)
        })

        registerViewModel.isMobileNumberEmpty.observe(this, Observer { isMobileNumberEmpty ->
            if (isMobileNumberEmpty) input_mobile_number.error =
                getString(R.string.signup_no_mobile)
            nested_scroll_view.requestChildFocus(input_mobile_number, input_mobile_number)
        })

        registerViewModel.isEmailEmpty.observe(this, Observer { isEmailEmpty ->
            if (isEmailEmpty) input_email.error = getString(R.string.signup_no_email)
        })

        registerViewModel.isPasswordEmpty.observe(this, Observer { isPasswordEmpty ->
            if (isPasswordEmpty) input_password.error = getString(R.string.signup_no_password)
        })

        registerViewModel.isConfirmPasswordEmpty.observe(this, Observer { isConfirmPasswordEmpty ->
            if (isConfirmPasswordEmpty) input_confirm_password.error =
                getString(R.string.signup_no_password)
        })

        registerViewModel.isPasswordMismatch.observe(this, Observer { isPasswordMismatch ->
            if (isPasswordMismatch) {
                Toast.makeText(
                    this,
                    getString(R.string.signup_password_mismatch),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        registerViewModel.isPolicyCheck.observe(this, Observer { isPolicyCheck ->
            if (!isPolicyCheck) {
                Toast.makeText(this, getString(R.string.signup_no_consent), Toast.LENGTH_SHORT)
                    .show()
            }
        })

        registerViewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                showVerificationScreen()
            } else {
                viewModelLogin.subscribeToken()
            }
        })

        registerViewModel.errorMessage.observe(this, Observer { errorMessage ->
            Log.e("DEBUG", "registration error")
            Toast.makeText(this, "$errorMessage", Toast.LENGTH_SHORT).show()
            showMessageDialog(errorMessage)
        })

        viewModelLogin.refreshLogin.observe(this, Observer { isRefreshLogin ->
            if (isRefreshLogin) {
                Log.e("DEBUG", "isRefreshLogin")
                registerViewModel.subscribeRegister(
                    input_fname.text.toString(),
                    input_lname.text.toString(),
                    input_gender.text.toString(),
                    input_date_of_birth.text.toString(),
                    input_id_number.text.toString(),
                    input_mobile_number.text.toString(),
                    input_email.text.toString(),
                    input_password.text.toString(),
                    input_confirm_password.text.toString(),
                    input_policy.isChecked
                )
            }
        })

        val customDropDownAdapter = ProvinceAdapter(this@RegisterActivity, arrayListOf(Province("", "---------- Select ----------")))
        spinner.apply {
            spinner.adapter = customDropDownAdapter
            spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                    val item = customDropDownAdapter.getItem(position) as Province
                    Timber.tag("DEBUG").e("item ${item.provinceName}")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

        registerViewModel.provinces.observe(this, {
            if (it.isNotEmpty()) {
                customDropDownAdapter.updateAdapter(it)
            }
        })

    }

    private fun iniSpinner() {

    }

}