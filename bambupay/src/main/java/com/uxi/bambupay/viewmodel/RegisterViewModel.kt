package com.uxi.bambupay.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.R
import com.uxi.bambupay.model.ResultWithMessage
import com.uxi.bambupay.model.SingleEvent
import com.uxi.bambupay.model.registration.*
import com.uxi.bambupay.repository.RegisterRepository
import com.uxi.bambupay.utils.Constants.Companion.PASSWORD_MIN_LENGTH
import com.uxi.bambupay.utils.FilePickerManager
import com.uxi.bambupay.utils.Utils
import com.uxi.bambupay.utils.convertDoB
import com.uxi.bambupay.view.ext.asListOfType
import io.reactivex.Flowable
import io.reactivex.rxkotlin.addTo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/19/20.
 * hunterxer31@gmail.com
 */
class RegisterViewModel @Inject
constructor(
    private val repository: RegisterRepository,
    private val utils: Utils,
    private val application: Application
) :
    BaseViewModel() {

    val isFirstNameEmpty = MutableLiveData<Boolean>()
    val isLastNameEmpty = MutableLiveData<Boolean>()

    private val _isGenderEmpty = MutableLiveData<SingleEvent<Boolean>>()
    val isGenderEmpty: LiveData<SingleEvent<Boolean>> = _isGenderEmpty
    val isDobEmpty = MutableLiveData<Boolean>()
    private val _isPobEmpty = MutableLiveData<SingleEvent<Boolean>>()
    val isPobEmpty: LiveData<SingleEvent<Boolean>> = _isPobEmpty
    val isIdNumberEmpty = MutableLiveData<Boolean>()
    val isMobileNumberEmpty = MutableLiveData<Boolean>()
    val isEmailEmpty = MutableLiveData<Boolean>()
    val isPasswordEmpty = MutableLiveData<Boolean>()
    val isConfirmPasswordEmpty = MutableLiveData<Boolean>()
    val isPasswordMismatch = MutableLiveData<Boolean>()
    val isPolicyCheck = MutableLiveData<Boolean>()

    private val _emptyFieldErrorMsg = MutableLiveData<SingleEvent<String>>()
    val emptyFieldErrorMsg: LiveData<SingleEvent<String>> = _emptyFieldErrorMsg

    private val _provinces = MutableLiveData<MutableList<Province>>()
    val provinces: LiveData<MutableList<Province>> = _provinces

    private val _works = MutableLiveData<List<Work>>()
    val works: LiveData<List<Work>> = _works

    private val _idTypes = MutableLiveData<List<IDType>>()
    val idTypes: LiveData<List<IDType>> = _idTypes

    private val _funds = MutableLiveData<List<Fund>>()
    val funds: LiveData<List<Fund>> = _funds

    private val _profileImageFile = MutableLiveData<File?>()
    val postProfileImageFile: LiveData<File?> = _profileImageFile

    private val _frontImageFile = MutableLiveData<File?>()
    val postFrontImageFile: LiveData<File?> = _frontImageFile

    private val _backImageFile = MutableLiveData<File?>()
    val postBackImageFile: LiveData<File?> = _backImageFile

    private val _documentFile = MutableLiveData<FilePickerManager.Result.Document>()
    val postDocumentFile: LiveData<FilePickerManager.Result.Document> = _documentFile

    private val _successRegistration = MutableLiveData<String>()
    val successRegistration: LiveData<String> = _successRegistration

    fun subscribeRegister(
        firstName: String?, lastName: String?, gender: String?,
        dateOfBirth: String?, mobileNumber: String?, email: String?,
        password: String?, confirmPassword: String?, isAccepted: Boolean,
        houseNo: String?, street: String?, brgy: String?, city: String?,
        provinceId: String?, postalCode: String?, placeOfBirth: String?,
        sourceOfFunds: String?, natureOfWork: String?, idType: String?,
        idNumber: String?, idExpirationDate: String?, agentCode: String?
    ) {

        Timber.tag("DEBUG").e("houseNo:: $houseNo")
        Timber.tag("DEBUG").e("street:: $street")
        Timber.tag("DEBUG").e("brgy:: $brgy")
        Timber.tag("DEBUG").e("city:: $city")
        Timber.tag("DEBUG").e("provinceId:: $provinceId")
        Timber.tag("DEBUG").e("others:: $postalCode")
        Timber.tag("DEBUG").e("mobileNumber:: $mobileNumber")

        if (firstName.isNullOrEmpty()) {
            isFirstNameEmpty.value = true
            return
        }

        if (lastName.isNullOrEmpty()) {
            isLastNameEmpty.value = true
            return
        }

        if (mobileNumber.isNullOrEmpty()) {
            _emptyFieldErrorMsg.value = SingleEvent(application.getString(R.string.signup_no_mobile))
        }

        if (!utils.isValidPhone(mobileNumber!!)) {
            isMobileNumberEmpty.value = true
            return
        }

        if (email.isNullOrEmpty()) {
            _emptyFieldErrorMsg.value = SingleEvent(application.getString(R.string.signup_no_email))
        }

        if (!utils.isEmailValid(email!!)) {
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

        if (dateOfBirth.isNullOrEmpty()) {
            isDobEmpty.value = true
            return
        }

        if (placeOfBirth.isNullOrEmpty()) {
            _isPobEmpty.value = SingleEvent(true)
            return
        }

        if (gender.isNullOrEmpty()) {
            _isGenderEmpty.value = SingleEvent(true)
            return
        }

        if (houseNo.isNullOrEmpty()) {
            _emptyFieldErrorMsg.value = SingleEvent(application.getString(R.string.signup_no_house_no))
            return
        }

        if (street.isNullOrEmpty()) {
            _emptyFieldErrorMsg.value = SingleEvent(application.getString(R.string.signup_no_street))
            return
        }

        if (brgy.isNullOrEmpty()) {
            _emptyFieldErrorMsg.value = SingleEvent(application.getString(R.string.signup_no_brgy))
            return
        }

        if (city.isNullOrEmpty()) {
            _emptyFieldErrorMsg.value = SingleEvent(application.getString(R.string.signup_no_city))
            return
        }

        if (postalCode.isNullOrEmpty()) {
            _emptyFieldErrorMsg.value = SingleEvent(application.getString(R.string.signup_no_postal_code))
            return
        }

        /*if (sourceOfFunds.isNullOrEmpty()) {
            return
        }

        if (natureOfWork.isNullOrEmpty()) {
            return
        }

        if (idType.isNullOrEmpty()) {
            return
        }*/

        if (idNumber.isNullOrEmpty()) {
//            isIdNumberEmpty.value = true
            _emptyFieldErrorMsg.value = SingleEvent(application.getString(R.string.signup_no_id_number))
            return
        }

        if (idExpirationDate.isNullOrEmpty()) {
            _emptyFieldErrorMsg.value = SingleEvent(application.getString(R.string.signup_no_id_expiration_date))
            return
        }

        if (agentCode.isNullOrEmpty()) {
            _emptyFieldErrorMsg.value = SingleEvent(application.getString(R.string.signup_no_agent_code))
            return
        }

        if (postFrontImageFile.value == null) {
            _emptyFieldErrorMsg.value = SingleEvent(application.getString(R.string.signup_no_id_front))
            return
        }

        if (postBackImageFile.value == null) {
            _emptyFieldErrorMsg.value = SingleEvent(application.getString(R.string.signup_no_id_back))
            return
        }

        if (!isAccepted) {
            isPolicyCheck.value = false
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

        val imageProfile = postProfileImageFile.value
        val imageIdFront = postFrontImageFile.value
        val imageIdBack = postBackImageFile.value

        Timber.tag("DEBUG").e("dateOfBirth:: $dateOfBirth")
        Timber.tag("DEBUG").e("idExpirationDate:: $idExpirationDate")
        val dob = convertDoB(dateOfBirth)
        val idExpiration = convertDoB(idExpirationDate)
        Timber.tag("DEBUG").e("DOB:: $dob")
        Timber.tag("DEBUG").e("idExpiration:: $idExpiration")
        Timber.tag("DEBUG").e("GENDER:: $gender")
        Timber.tag("DEBUG").e("phoneNumber:: $phoneNumber")
        Timber.tag("DEBUG").e("image:: $imageProfile")

        val map: HashMap<String, RequestBody> = HashMap()
        map["first_name"] = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
        map["last_name"] = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
        map["dob"] = dob!!.toRequestBody("text/plain".toMediaTypeOrNull())
        map["pob"] = placeOfBirth.toRequestBody("text/plain".toMediaTypeOrNull())
        map["email_address"] = email.toRequestBody("text/plain".toMediaTypeOrNull())
        map["mobile_no"] = (phoneNumber ?: "").toRequestBody("text/plain".toMediaTypeOrNull())
        map["password"] = encryptedPassword.toRequestBody("text/plain".toMediaTypeOrNull())
        map["house_no"] = houseNo.toRequestBody("text/plain".toMediaTypeOrNull())
        map["street"] = street.toRequestBody("text/plain".toMediaTypeOrNull())
        map["brgy"] = brgy.toRequestBody("text/plain".toMediaTypeOrNull())
        map["city"] = city.toRequestBody("text/plain".toMediaTypeOrNull())
        map["country_id"] = "169".toRequestBody("text/plain".toMediaTypeOrNull())
        map["postal_code"] = postalCode.toRequestBody("text/plain".toMediaTypeOrNull())
        map["source_of_funds"] = sourceOfFunds!!.toRequestBody("text/plain".toMediaTypeOrNull())
        map["nature_of_work"] = natureOfWork!!.toRequestBody("text/plain".toMediaTypeOrNull())
        map["id_type"] = idType!!.toRequestBody("text/plain".toMediaTypeOrNull())
        map["id_no"] = idNumber.toRequestBody("text/plain".toMediaTypeOrNull())
        map["id_expiration_date"] = idExpiration!!.toRequestBody("text/plain".toMediaTypeOrNull())
        map["agent_code"] = agentCode.toRequestBody("text/plain".toMediaTypeOrNull())

        if (!tempGender.isNullOrEmpty()) {
            map["gender"] = tempGender.toRequestBody("text/plain".toMediaTypeOrNull())
        }

        if (!provinceId.isNullOrEmpty()) {
            map["province_id"] = provinceId.toRequestBody("text/plain".toMediaTypeOrNull())
        }

        /*repository.loadRegister(map, imageProfile, imageIdFront, imageIdBack)
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
            .addTo(disposable)*/

        repository.loadRegister(map, imageProfile, imageIdFront, imageIdBack)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                resultState(it)
            }, Timber::e)
            .addTo(disposable)
    }

    fun subscribeProvince() {
        repository.loadProvinces()
            .subscribe({
                it.response?.let { it1 ->
                    _provinces.value = it1.toMutableList()
                }
            }, {
                Timber.e(it)
                if (refreshToken(it)) {
                    isSuccess.value = false
                }
            })
            .addTo(disposable)
    }

    fun subscribeTools() {
        val worksApi = repository.loadWorks()
        val idTypesApi = repository.loadIDTypes()
        val fundsApi = repository.loadFunds()

        val zip = Flowable.zip(worksApi, idTypesApi, fundsApi, { t1, t2, t3 ->
            Wrapper(t1, t2, t3)
        })

        zip
            .subscribe({
                resultState(it.works)
                resultState(it.idTypes)
                resultState(it.funds)
            }, Timber::e)
            .addTo(disposable)
    }

    private fun <T : Any> resultState(t: ResultWithMessage<T>) {
        when (t) {
            is ResultWithMessage.Success -> {
                when (t.value) {
                    is List<*> -> {
                        when (t.value.firstOrNull()) {
                            is Work -> {
                                val works = t.value.asListOfType<Work>()
                                Timber.tag("DEBUG").e("WORK:: ${works?.size}")
                                _works.value = works
                            }
                            is IDType -> {
                                val idTypes = t.value.asListOfType<IDType>()
                                Timber.tag("DEBUG").e("ID TYPES:: ${idTypes?.size}")
                                _idTypes.value = idTypes
                            }
                            is Fund -> {
                                val funds = t.value.asListOfType<Fund>()
                                Timber.tag("DEBUG").e("FUNDS:: ${funds?.size}")
                                _funds.value = funds
                            }
                        }
                    }
                    is Registration -> {
                        Timber.tag("DEBUG").e("Success Registration!")
                        _successRegistration.value = t.message
                    }
                }
            }
            is ResultWithMessage.Error -> {
                val errorMessage = t.message
                Timber.tag("DEBUG").e("Error:: $errorMessage")
            }
        }
    }

    inner class Wrapper(
        val works: ResultWithMessage<List<Work>>,
        val idTypes: ResultWithMessage<List<IDType>>,
        val funds: ResultWithMessage<List<Fund>>
    )

    fun setPostImageFile(file: File?, imageType: ImageType) {
        when (imageType) {
            ImageType.PROFILE -> setPostProfileImageFile(file)
            ImageType.ID_FRONT -> setPostFrontImageFile(file)
            ImageType.ID_BACK -> setPostBackImageFile(file)
        }
    }

    fun setPostProfileImageFile(file: File?) {
        Timber.tag("DEBUG").e("Profile FILE:: $file")
        _profileImageFile.postValue(file)
    }

    fun setPostFrontImageFile(file: File?) {
        Timber.tag("DEBUG").e("Front ID FILE:: $file")
        _frontImageFile.postValue(file)
    }

    fun setPostBackImageFile(file: File?) {
        Timber.tag("DEBUG").e("Back ID FILE:: $file")
        _backImageFile.postValue(file)
    }


    fun setPostDocumentFile(document: FilePickerManager.Result.Document?) {
        _documentFile.postValue(document)
    }

}