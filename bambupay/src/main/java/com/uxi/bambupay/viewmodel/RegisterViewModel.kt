package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.model.Province
import com.uxi.bambupay.repository.RegisterRepository
import com.uxi.bambupay.utils.Constants.Companion.PASSWORD_MIN_LENGTH
import com.uxi.bambupay.utils.FilePickerManager
import com.uxi.bambupay.utils.Utils
import com.uxi.bambupay.utils.convertDoB
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/19/20.
 * hunterxer31@gmail.com
 */
class RegisterViewModel @Inject
constructor(private val repository: RegisterRepository, private val utils: Utils) :
    BaseViewModel() {

    val isFirstNameEmpty = MutableLiveData<Boolean>()
    val isLastNameEmpty = MutableLiveData<Boolean>()

    //    val isGenderEmpty = MutableLiveData<Boolean>()
    val isDobEmpty = MutableLiveData<Boolean>()
    val isIdNumberEmpty = MutableLiveData<Boolean>()
    val isMobileNumberEmpty = MutableLiveData<Boolean>()
    val isEmailEmpty = MutableLiveData<Boolean>()
    val isPasswordEmpty = MutableLiveData<Boolean>()
    val isConfirmPasswordEmpty = MutableLiveData<Boolean>()
    val isPasswordMismatch = MutableLiveData<Boolean>()
    val isPolicyCheck = MutableLiveData<Boolean>()
    val provinces = MutableLiveData<MutableList<Province>>()

    private val _imageFile = MutableLiveData<File?>()
    val postImageFile: LiveData<File?> = _imageFile

    private val _documentFile = MutableLiveData<FilePickerManager.Result.Document>()
    val postDocumentFile: LiveData<FilePickerManager.Result.Document> = _documentFile

    fun subscribeRegister(
        firstName: String?, lastName: String?, gender: String?,
        dateOfBirth: String?, mobileNumber: String?, email: String?,
        password: String?, confirmPassword: String?, isAccepted: Boolean,
        houseNo: String?, street: String?, brgy: String?, city: String?,
        provinceId: String?, others: String?
    ) {

        Timber.tag("DEBUG").e("houseNo:: $houseNo")
        Timber.tag("DEBUG").e("street:: $street")
        Timber.tag("DEBUG").e("brgy:: $brgy")
        Timber.tag("DEBUG").e("city:: $city")
        Timber.tag("DEBUG").e("provinceId:: $provinceId")
        Timber.tag("DEBUG").e("others:: $others")

        if (firstName.isNullOrEmpty()) {
            isFirstNameEmpty.value = true
            return
        }

        if (lastName.isNullOrEmpty()) {
            isLastNameEmpty.value = true
            return
        }

//        if (gender.isNullOrEmpty()) {
////            isGenderEmpty.value = true
//            return
//        }

        if (dateOfBirth.isNullOrEmpty()) {
            isDobEmpty.value = true
            return
        }

//        if (idNumber.isNullOrEmpty()) {
//            isIdNumberEmpty.value = true
//            return
//        }

        if (!utils.isValidPhone(mobileNumber!!)) {
            isMobileNumberEmpty.value = true
            return
        }

        if (email.isNullOrEmpty() || !utils.isEmailValid(email)) {
            isEmailEmpty.value = true
            return
        }

        if (password?.length!! < PASSWORD_MIN_LENGTH) {
            isPasswordEmpty.value = true
            return
        }

        if (confirmPassword?.length!! < PASSWORD_MIN_LENGTH) {
            isConfirmPasswordEmpty.value = true
            return
        }

        if (password != confirmPassword) {
            isPasswordMismatch.value = true
            return
        }

        if (!isAccepted) {
            isPolicyCheck.value = false
            return
        }

        if (houseNo.isNullOrEmpty()) {
            return
        }

        if (street.isNullOrEmpty()) {
            return
        }

        if (brgy.isNullOrEmpty()) {
            return
        }

        if (city.isNullOrEmpty()) {
            return
        }

        var tempGender: String? = null
        if (!gender.isNullOrEmpty()) {
            if (gender == "Male") {
                tempGender = "2"
            } else if (gender == "Female") {
                tempGender = "1"
            }
        }

//        var countryIsoCode = utils.getCountryIsoCode(mobileNumber)
//        if (countryIsoCode == null) {
//            countryIsoCode = "63"
//        }

        val tempNumber: String = if (mobileNumber.startsWith("09")) {
            mobileNumber.replaceFirst("0", "63")
        } else {
            mobileNumber
        }

        val phoneNumber = utils.getMobileNumber(tempNumber)

        val encryptedPassword = utils.sha256(password)

        val image = postImageFile.value

        Timber.tag("DEBUG").e("dateOfBirth:: $dateOfBirth")
        val dob = convertDoB(dateOfBirth)
        Timber.tag("DEBUG").e("DOB:: $dob")
        Timber.tag("DEBUG").e("GENDER:: $gender")
        Timber.tag("DEBUG").e("phoneNumber:: $phoneNumber")
        Timber.tag("DEBUG").e("image:: $image")

        val map: HashMap<String, RequestBody> = HashMap()
        map["first_name"] = RequestBody.create("text/plain".toMediaTypeOrNull(), firstName)
        map["last_name"] = RequestBody.create("text/plain".toMediaTypeOrNull(), lastName)
        map["dob"] = RequestBody.create("text/plain".toMediaTypeOrNull(), dob!!)
        map["email_address"] = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
        map["mobile_no"] = RequestBody.create("text/plain".toMediaTypeOrNull(), phoneNumber ?: "")
        map["password"] = RequestBody.create("text/plain".toMediaTypeOrNull(), encryptedPassword)
        map["country_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), "169")
        map["house_no"] = RequestBody.create("text/plain".toMediaTypeOrNull(), houseNo)
        map["street"] = RequestBody.create("text/plain".toMediaTypeOrNull(), street)
        map["brgy"] = RequestBody.create("text/plain".toMediaTypeOrNull(), brgy)
        map["city"] = RequestBody.create("text/plain".toMediaTypeOrNull(), city)

        // Optional
        if (!tempGender.isNullOrEmpty()) {
            map["gender"] = RequestBody.create("text/plain".toMediaTypeOrNull(), tempGender)
        }

        if (!provinceId.isNullOrEmpty()) {
            map["province_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), provinceId)
        }

        if (!others.isNullOrEmpty()) {
            map["others"] = RequestBody.create("text/plain".toMediaTypeOrNull(), others)
        }

        disposable?.add(repository.loadRegister(map, image)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({

                if (it.response != null) {

                } else {
                    if (it.error == true) {
                        it.errorMessage?.let { error ->
                            errorMessage.value = error
                            Log.e("DEBUG", "error message:: $error")
                        }
                        it.successMessage?.let { error ->
                            errorMessage.value = error
                        }
                    } else {
                        isSuccess.value = true
                    }
                }

            }, {
                Timber.e(it)
                if (refreshToken(it)) {
                    Log.e("DEBUG", "error refreshToken")
                    utils.saveTokenPack("", true)
                    isSuccess.value = false
                }
            })
        )

    }

    fun subscribeProvince() {
        disposable?.add(
            repository.loadProvinces()
                .subscribe({
                    it.response?.let { it1 ->
                        provinces.value = it1.toMutableList()
                    }
                }, {
                    Timber.e(it)
                    if (refreshToken(it)) {
                        isSuccess.value = false
                    }
                })
        )
    }

    fun setPostImageFile(file: File?) {
        Timber.tag("DEBUG").e("FILE:: $file")
        _imageFile.postValue(file)
    }

    fun setPostDocumentFile(document: FilePickerManager.Result.Document?) {
        _documentFile.postValue(document)
    }

}