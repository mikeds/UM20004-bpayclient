package com.uxi.bambupay.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupay.api.RequestRegister
import com.uxi.bambupay.model.Province
import com.uxi.bambupay.repository.RegisterRepository
import com.uxi.bambupay.utils.Constants.Companion.PASSWORD_MIN_LENGTH
import com.uxi.bambupay.utils.Utils
import timber.log.Timber
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
//    val isDobEmpty = MutableLiveData<Boolean>()
    val isIdNumberEmpty = MutableLiveData<Boolean>()
    val isMobileNumberEmpty = MutableLiveData<Boolean>()
    val isEmailEmpty = MutableLiveData<Boolean>()
    val isPasswordEmpty = MutableLiveData<Boolean>()
    val isConfirmPasswordEmpty = MutableLiveData<Boolean>()
    val isPasswordMismatch = MutableLiveData<Boolean>()
    val isPolicyCheck = MutableLiveData<Boolean>()
    val provinces = MutableLiveData<MutableList<Province>>()

    fun subscribeRegister(
        firstName: String?,
        lastName: String?,
        gender: String?,
        dateOfBirth: String?,
        idNumber: String?,
        mobileNumber: String?,
        email: String?,
        password: String?,
        confirmPassword: String?,
        isAccepted: Boolean
    ) {

        if (firstName.isNullOrEmpty()) {
            isFirstNameEmpty.value = true
            return
        }

        if (lastName.isNullOrEmpty()) {
            isLastNameEmpty.value = true
            return
        }

        if (gender.isNullOrEmpty()) {
//            isGenderEmpty.value = true
            return
        }

        if (dateOfBirth.isNullOrEmpty()) {
//            isDobEmpty.value = true
            return
        }

        /*if (idNumber.isNullOrEmpty()) {
            isIdNumberEmpty.value = true
            return
        }*/

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

        var countryIsoCode = utils.getCountryIsoCode(mobileNumber)
        if (countryIsoCode == null) {
            countryIsoCode = "63"
        }

        val tempNumber: String = if (mobileNumber.startsWith("09")) {
            mobileNumber.replaceFirst("0", "63")
        } else {
            mobileNumber
        }
        val phoneNumber = utils.getMobileNumber(tempNumber)

        val encryptedPassword = utils.sha256(password)

        val requestBody = RequestRegister.Builder()
            .setFirstName(firstName)
            .setLastName(lastName)
            .setEmail(email)
            .setMobileNumber(phoneNumber!!)
            .setMobileCountryCode(countryIsoCode)
            .setPassword(encryptedPassword)
            .build()

        disposable?.add(repository.loadRegister(requestBody)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({

                if (it.response != null) {

                } else {
                    if (it.error == true) {
                        it.message?.let { error ->
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

}