package com.uxi.bambupay.api

import com.google.gson.annotations.SerializedName

/**
 * Created by Eraño Payawal on 7/19/20.
 * hunterxer31@gmail.com
 */
open class RequestRegister private constructor(
    @SerializedName("first_name")
    private val firstName: String?,

    @SerializedName("last_name")
    private val lastName: String?,

    @SerializedName("email_address")
    private val email: String?,

    @SerializedName("mobile_no")
    private val mobileNumber: String?,

    private val password: String?,

    @SerializedName("country_id")
    private val countryId: String?,

    @SerializedName("province_id")
    private val provinceId: String?,

    private val gender: String?,

    private val dob: String?,

    @SerializedName("house_no")
    private val houseNo: String?,

    private val street: String?,

    private val brgy: String?,

    private val city: String?,

    private val others: String?

    ) {

    data class Builder(
        @SerializedName("first_name")
        private var firstName: String? = null,

        @SerializedName("last_name")
        private var lastName: String? = null,

        @SerializedName("email_address")
        private var email: String? = null,

        @SerializedName("mobile_no")
        private var mobileNumber: String? = null,

        private var password: String? = null,

        @SerializedName("country_id")
        private var countryId: String? = null,

        @SerializedName("province_id")
        private var provinceId: String? = null,

        private var gender: String? = null,

        private var dob: String? = null,

        @SerializedName("house_no")
        private var houseNo: String? = null,

        private var street: String? = null,

        private var brgy: String? = null,

        private var city: String? = null,

        private var others: String? = null) {

        fun setFirstName(firstName: String) = apply { this.firstName = firstName }
        fun setLastName(lastName: String) = apply { this.lastName = lastName }
        fun setEmail(email: String) = apply { this.email = email }
        fun setMobileNumber(mobileNumber: String) = apply { this.mobileNumber = mobileNumber }
        fun setPassword(password: String) = apply { this.password = password }
        fun setCountryId(countryId: String) = apply { this.countryId = countryId }
        fun setProvinceId(provinceId: String) = apply { this.provinceId = provinceId }
        fun setGender(gender: String) = apply { this.gender = gender }
        fun setDoB(dob: String) = apply { this.dob = dob }
        fun setHouseNo(houseNo: String) = apply { this.houseNo = houseNo }
        fun setStreet(street: String) = apply { this.street = street }
        fun setBrgy(brgy: String) = apply { this.brgy = brgy }
        fun setCity(city: String) = apply { this.city = city}
        fun setOthers(others: String) = apply { this.others = others }
        fun build() = RequestRegister(firstName, lastName, email, mobileNumber, password, countryId, provinceId, gender, dob, houseNo, street, brgy, city, others)
    }

}