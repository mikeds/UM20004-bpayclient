package com.uxi.bambupay.view.activity

//import com.microblink.entities.recognizers.RecognizerBundle
//import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer
//import com.microblink.entities.recognizers.blinkid.imageoptions.FullDocumentImageOptions
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.net.Uri
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
import com.uxi.bambupay.BuildConfig
import com.uxi.bambupay.R
import com.uxi.bambupay.model.registration.*
import com.uxi.bambupay.utils.BitmapUtils
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.utils.FilePickerManager
import com.uxi.bambupay.utils.formatDate
import com.uxi.bambupay.view.adapter.FundsAdapter
import com.uxi.bambupay.view.adapter.IdentificationAdapter
import com.uxi.bambupay.view.adapter.NatureWorkAdapter
import com.uxi.bambupay.view.adapter.ProvinceAdapter
import com.uxi.bambupay.viewmodel.LoginViewModel
import com.uxi.bambupay.viewmodel.RegisterViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_register.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class RegisterActivity : BaseActivity(), View.OnClickListener {

    private val registerViewModel by viewModel<RegisterViewModel>()
    private val viewModelLogin by viewModel<LoginViewModel>()

    @Inject
    lateinit var filePickerManager: FilePickerManager

    companion object {
        const val MY_BLINKID_REQUEST_CODE = 123
    }

//    private var recognizer: BlinkIdCombinedRecognizer? = null
//    private var recognizerBundle: RecognizerBundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        observeViewModel()
        events()
//        setupMicroBlink()
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
//                onScanSuccess(data)
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

    private fun register() {
        registerViewModel.subscribeRegister(
            input_fname.text.toString(),
            input_lname.text.toString(),
            input_gender.text.toString(),
            input_date_of_birth.text.toString(),
            input_mobile_number.text.toString(),
            input_email.text.toString(),
            input_password.text.toString(),
            input_confirm_password.text.toString(),
            input_policy.isChecked,
            input_text_house_no.text.toString(),
            input_text_street.text.toString(),
            input_text_brgy.text.toString(),
            input_text_city.text.toString(),
            provinceSelected?.provinceId,
            input_text_postal_code.text.toString(),
            input_pob.text.toString(),
            fundSelected.id,
            workSelected.id,
            idTypeSelected.id,
            input_text_id_number.text.toString(),
            input_id_expiration.text.toString(),
            input_text_agent_code.text.toString()
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                register()
            }
            R.id.input_gender -> {
                showGenderDialog()
            }
            R.id.input_date_of_birth -> {
                showBirthdDatePicker()
            }
            R.id.input_id_expiration -> {
                showExpirationDatePicker()
            }
            R.id.btn_terms -> {
                // https://dev-tms.resolveitthrough.us/terms-and-conditions
                val url = "${BuildConfig.API_BASE_URL_TMS}terms-and-conditions"
                openBrowser(url)
            }
            R.id.btn_data_privacy -> {
                // https://dev-tms.resolveitthrough.us/data-privacy
                val url = "${BuildConfig.API_BASE_URL_TMS}data-privacy"
                openBrowser(url)
            }
            R.id.btn_eula -> {
                // https://dev-tms.resolveitthrough.us/eula
                val url = "${BuildConfig.API_BASE_URL_TMS}eula"
                openBrowser(url)
            }
        }
    }

    private fun openBrowser(url: String?) {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(it)
            startActivity(intent)
        }
    }

    private fun events() {
        //input_card_expiry.addTextChangedListener(CreditCardExpiryTextWatcher(input_card_expiry))
        btn_image_profile.setOnClickListener {
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
                takeFile(FilePickerManager.Type.CAMERA, ImageType.PROFILE)
                dialog.dismiss()
            }
            btnChooseGallery?.setOnClickListener {
                takeFile(FilePickerManager.Type.GALLERY, ImageType.PROFILE)
                dialog.dismiss()
            }
            dialog.show()

        }

        img_id_front_view.setOnClickListener {
            showBottomSheet(
                onTakePhotoClick = { takeFile(FilePickerManager.Type.CAMERA, ImageType.ID_FRONT) },
                onChooseGalleryClick = { takeFile(FilePickerManager.Type.GALLERY, ImageType.ID_FRONT) }
            )
        }

        img_id_back_view.setOnClickListener {
            showBottomSheet(
                onTakePhotoClick = { takeFile(FilePickerManager.Type.CAMERA, ImageType.ID_BACK) },
                onChooseGalleryClick = { takeFile(FilePickerManager.Type.GALLERY, ImageType.ID_BACK) }
            )
        }

        btn_register.setOnClickListener(this)
        input_gender.setOnClickListener(this)
        input_date_of_birth.setOnClickListener(this)
        input_id_expiration.setOnClickListener(this)
        btn_terms.setOnClickListener(this)
        btn_data_privacy.setOnClickListener(this)
        btn_eula.setOnClickListener(this)
//            showVerificationScreen()
    }

    private fun showBottomSheet( onTakePhotoClick: () -> Unit, onChooseGalleryClick: () -> Unit) {
        val dialogView: View = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogView)
        val btnTakePhoto = dialogView.findViewById<TextView>(R.id.btn_take_photo)
        val btnChooseGallery = dialogView.findViewById<TextView>(R.id.btn_choose_gallery)
        btnTakePhoto?.setOnClickListener {
            onTakePhotoClick.invoke()
            dialog.dismiss()
        }
        btnChooseGallery?.setOnClickListener {
            onChooseGalleryClick.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }

    /*private fun setupMicroBlink() {
        // we'll use Machine Readable Travel Document recognizer
        recognizer = BlinkIdCombinedRecognizer()

        val options = recognizer as FullDocumentImageOptions
        options.setReturnFullDocumentImage(true)

        // put our recognizer in bundle so that it can be sent via intent
        recognizerBundle = RecognizerBundle(recognizer)
    }*/

    /*private fun onScanSuccess(data: Intent) {
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

    }*/

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

    private fun showBirthdDatePicker() {
        val mCalendar: Calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this, R.style.DatePickerDialogStyle,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                mCalendar.set(Calendar.YEAR, year)
                mCalendar.set(Calendar.MONTH, monthOfYear)
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date: String = formatDate(mCalendar.time)
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

    private fun showExpirationDatePicker() {
        val mCalendar: Calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this, R.style.DatePickerDialogStyle,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                mCalendar.set(Calendar.YEAR, year)
                mCalendar.set(Calendar.MONTH, monthOfYear)
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date: String = formatDate(mCalendar.time)
                input_id_expiration.setText(date)
                input_id_expiration.error = null
            },
            mCalendar.get(Calendar.YEAR),
            mCalendar.get(Calendar.MONTH),
            mCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setTitle("Select Date")
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun observeViewModel() {
        viewModelLogin.subscribeToken()
        registerViewModel.subscribeProvince()
        registerViewModel.subscribeTools()

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

        registerViewModel.isGenderEmpty.observe(this, { it1 ->
            it1?.getContentIfNotHandled()?.let {
                if (it) {
                    input_gender.error = getString(R.string.signup_no_gender)
                    nested_scroll_view.requestChildFocus(input_gender, input_gender)
                }
            }
        })

        registerViewModel.isDobEmpty.observe(this, Observer { isDobEmpty ->
            if (isDobEmpty) input_date_of_birth.error = getString(R.string.signup_no_dob)
            nested_scroll_view.requestChildFocus(input_date_of_birth, input_date_of_birth)
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

        registerViewModel.isPobEmpty.observe(this, { it1 ->
            it1?.getContentIfNotHandled()?.let {
                if (it) {
                    showMessageDialog(getString(R.string.signup_no_pob))
                }
            }
        })

        registerViewModel.emptyFieldErrorMsg.observe(this, { it1 ->
            it1?.getContentIfNotHandled()?.let {
                showMessageDialog(it)
            }
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
//                showVerificationScreen()
                showOtpScreen()
            } else {
                viewModelLogin.subscribeToken()
            }
        })

        registerViewModel.successRegistration.observe(this, {
            it?.let {
                showMessageDialog(
                    message = it,
                    onDismiss = ::onDialogOkay
                )
            }
        })

        registerViewModel.errorMessage.observe(this, Observer { errorMessage ->
            Log.e("DEBUG", "registration error")
            Toast.makeText(this, "$errorMessage", Toast.LENGTH_SHORT).show()
            showMessageDialog(errorMessage)
        })

        viewModelLogin.refreshLogin.observe(this, Observer { isRefreshLogin ->
            if (isRefreshLogin) {
                register()
            }
        })

        val customDropDownAdapter = ProvinceAdapter(this@RegisterActivity, arrayListOf(Province("", "---------- Select ----------")))
        spinner_state.apply {
            spinner_state.adapter = customDropDownAdapter
            spinner_state.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                    provinceSelected = customDropDownAdapter.getItem(position) as Province
                    Timber.tag("DEBUG").e("item ${provinceSelected.provinceName}")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

        registerViewModel.works.observe(this, {
            if (it.isNotEmpty()) {
                spinner_nature_work.apply {
                    val natureWorkAdapter = NatureWorkAdapter(this@RegisterActivity, it)
                    adapter = natureWorkAdapter
                    onItemSelectedListener = object : OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            workSelected = natureWorkAdapter.getItem(position)
                            Timber.tag("DEBUG").e("workSelected:: ${workSelected.name}")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }
                }
            }
        })

        registerViewModel.idTypes.observe(this,  {
            if (it.isNotEmpty()) {
                spinner_id_type.apply {
                    val idAdapter = IdentificationAdapter(this@RegisterActivity, it)
                    adapter = idAdapter
                    onItemSelectedListener = object : OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            idTypeSelected = idAdapter.getItem(position)
                            Timber.tag("DEBUG").e("idTypeSelected:: ${idTypeSelected.name}")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }
                }
            }
        })

        registerViewModel.funds.observe(this,  {
            if (it.isNotEmpty()) {
                spinner_source_funds.apply {
                    val fundAdapter = FundsAdapter(it)
                    adapter = fundAdapter
                    onItemSelectedListener = object : OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            fundSelected = fundAdapter.getItem(position)
                            Timber.tag("DEBUG").e("fundSelected:: ${fundSelected.name}")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }
                }
            }
        })

        registerViewModel.provinces.observe(this, {
            if (it.isNotEmpty()) {
                customDropDownAdapter.updateAdapter(it)
            }
        })

        registerViewModel.postProfileImageFile.observe(this, { file ->
            when (file) {
                null -> btn_image_profile.setImageResource(R.drawable.bg_img_preview)
                else -> {
                    btn_image_profile.setImageBitmap(BitmapUtils.getBitmap(file.absolutePath))
                    txt_capture.visibility = View.INVISIBLE
                }
            }
        })

        registerViewModel.postFrontImageFile.observe(this, { file ->
            when (file) {
                null -> img_id_front_view.setImageDrawable(null)
                else -> {
                    img_id_front_view.setImageBitmap(BitmapUtils.getBitmap(file.absolutePath))
                }
            }
        })

        registerViewModel.postBackImageFile.observe(this, { file ->
            when (file) {
                null -> img_id_back_view.setImageDrawable(null)
                else -> {
                    img_id_back_view.setImageBitmap(BitmapUtils.getBitmap(file.absolutePath))
                }
            }
        })

        registerViewModel.isIdNumberEmpty.observe(this, Observer { isIdNumberEmpty ->
            if (isIdNumberEmpty) input_text_id_number.error = getString(R.string.signup_no_id)
        })

    }

    private fun onDialogOkay() {
        showOtpScreen()
    }

    private lateinit var provinceSelected: Province
    private lateinit var workSelected: Work
    private lateinit var idTypeSelected: IDType
    private lateinit var fundSelected: Fund

    private fun takeFile(type: FilePickerManager.Type, imageType: ImageType) {
        filePickerManager.pickFile(this, type)
            .subscribe({ result ->
                when (result) {
                    is FilePickerManager.Result.Image -> {
                        registerViewModel.setPostImageFile(result.file, imageType)
                    }
                    is FilePickerManager.Result.Document -> registerViewModel.setPostDocumentFile(result)
                    FilePickerManager.Result.Cancelled -> Timber.d("file picker cancelled")
                }
            }, Timber::e)
            .addTo(disposeBag)
    }

    private fun showOtpScreen() {
        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra(Constants.SCREEN_FROM, Constants.REGISTRATION_SCREEN)
        intent.putExtra(Constants.MOBILE_NUMBER, input_mobile_number.text.toString())
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

}