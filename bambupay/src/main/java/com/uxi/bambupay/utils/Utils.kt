package com.uxi.bambupay.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import androidx.core.util.PatternsCompat
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.uxi.bambupay.R
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
class Utils constructor(private val context: Context?) {
    private val prefs: SharedPreferences

    fun sha256(input: String) = hashString("SHA-256", input)

    private fun printHexBinary(data: ByteArray): String {
        val r = StringBuilder(data.size * 2)
        data.forEach { b ->
            val i = b.toInt()
            r.append(HEX_CHARS[i shr 4 and 0xF])
            r.append(HEX_CHARS[i and 0xF])
        }
        return r.toString()
    }

    private fun hashString(type: String, input: String): String {
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        return printHexBinary(bytes).toLowerCase()
    }

    fun clearPref() {
        prefs.edit().clear().apply()
    }

    fun isEmailValid(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    fun isValidPhone(s: String?): Boolean {
        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        val p: Pattern = Pattern.compile("^[0-9]{10,13}\$")
        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        val m: Matcher = p.matcher(s!!)
        return m.find() && m.group() == s
    }

    fun getCountryIsoCode(number: String): String? {

        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val validatedNumber = if (number.startsWith("+")) number else "+$number"

        val phoneNumber = try {
            phoneNumberUtil.parse(validatedNumber, null)
        } catch (e: NumberParseException) {
            Log.e("DEBUG", "error during parsing a number")
            null
        }
            ?: return null

        phoneNumber
        return phoneNumber.countryCode.toString()//phoneNumberUtil.getRegionCodeForCountryCode(phoneNumber.countryCode)
    }

    fun getMobileNumber(number: String): String? {

        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val validatedNumber = if (number.startsWith("+")) number else "+$number"

        val phoneNumber = try {
            phoneNumberUtil.parse(validatedNumber, null)
        } catch (e: NumberParseException) {
            Log.e("DEBUG", "error during parsing a number")
            null
        }
            ?: return null

        return phoneNumber.nationalNumber.toString()
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,###.##")
        return formatter.format(amount.toDouble())
    }

    fun getIsoTimeStamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        return sdf.format(Date())
    }

    fun saveTokenPack(
        token: String?,
        expiryDate: Long,
        isLoggedIn: Boolean
    ) {
        prefs.edit().putString(TOKEN, token).apply()
        prefs.edit().putLong(TOKEN_EXPIRY_DATE, expiryDate).apply()
        prefs.edit().putBoolean(IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun saveTokenPack(token: String?, isExpired: Boolean) {
        prefs.edit().putString(TOKEN, token).apply()
        prefs.edit().putBoolean(IS_TOKEN_EXPIRED, isExpired).apply()
    }

    val token: String?
        get() = prefs.getString(TOKEN, "")

    fun saveToken(token: String?) {
        prefs.edit().putString(TOKEN, token).apply()
    }

    val isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGGED_IN, false)

    fun saveLoggedIn(isLoggedIn: Boolean) {
        prefs.edit().putBoolean(IS_LOGGED_IN, isLoggedIn).apply()
    }

    val isTokenExpired: Boolean
        get() = prefs.getBoolean(IS_TOKEN_EXPIRED, true)

    // has saw tutorial
    fun saveHasSawTutorial(hasSawTutorial: Boolean) {
        prefs.edit().putBoolean(HAS_SAW_TUTORIAL, hasSawTutorial).apply()
    }

    fun hasSawTutorial(): Boolean {
        return prefs.getBoolean(HAS_SAW_TUTORIAL, false)
    }

    // USER_EMAIL
    fun saveUserEmail(email: String?) {
        prefs.edit().putString(USER_EMAIL, email).apply()
    }

    val userEmail: String?
        get() = prefs.getString(USER_EMAIL, "")

    fun saveTokenExpired(isExpired: Boolean) {
        prefs.edit().putBoolean(IS_TOKEN_EXPIRED, isExpired).apply()
    }

    fun saveLastTransactionId(transactionId: String) {
        prefs.edit().putString(USER_LAST_TRANSACTION_ID, transactionId).apply()
    }

    //---------------------------------------------------------------------------
    fun saveUserTokenPack(token: String?, isExpired: Boolean) {
        prefs.edit().putString(USER_TOKEN, token).apply()
        prefs.edit().putBoolean(IS_USER_TOKEN_EXPIRED, isExpired).apply()
    }

    fun saveUserKeyPack(secretKey: String?, secretCode: String?) {
        prefs.edit().putString(USER_SECRET_KEY, secretKey).apply()
        prefs.edit().putString(USER_SECRET_CODE, secretCode).apply()
    }

    //---------------------------------------------------------------------------

    fun saveUbnLoginCode(code: String?) {
        prefs.edit().putString(USER_UBP_LOGIN_CODE, code).apply()
    }

    val isUserTokenExpired: Boolean
        get() = prefs.getBoolean(IS_USER_TOKEN_EXPIRED, true)

    val userToken: String?
        get() = prefs.getString(USER_TOKEN, "")

    val userSecretKey: String?
        get() = prefs.getString(USER_SECRET_KEY, "")

    val userSecretCode: String?
        get() = prefs.getString(USER_SECRET_CODE, "")

    val userLastTransactionId: String?
        get() = prefs.getString(USER_LAST_TRANSACTION_ID, "")

    val ubpClientId: String?
        get() = context?.getString(R.string.ubp_client_id)

    val ubpClientSecret: String?
        get() = context?.getString(R.string.ubp_client_secret)

    val ubpPartnerId: String?
        get() = context?.getString(R.string.ubp_partner_id)

    val ubpLoginCode: String?
        get() = prefs.getString(USER_UBP_LOGIN_CODE, "")
    //---------------------------------------------------------------------------


    companion object {
        const val BP_PREFS = "bp_prefs"
        const val TOKEN = "token"
        const val TOKEN_EXPIRY_DATE = "token_expiry_date"
        const val IS_TOKEN_EXPIRED = "is_token_expired"
        const val IS_LOGGED_IN = "is_logged_in"
        const val HAS_SAW_TUTORIAL = "has_saw_tutorial"
        const val USER_EMAIL = "user_email"
        const val USER_NAME = "user_name"
        const val USER_TOKEN = "user_token"
        const val IS_USER_TOKEN_EXPIRED = "is_user_token_expired"
        const val USER_SECRET_KEY = "user_secret_key"
        const val USER_SECRET_CODE = "user_secret_code"
        const val USER_LAST_TRANSACTION_ID = "user_last_transaction_id"
        const val USER_UBP_LOGIN_CODE = "user_ubp_login_code"
        val HEX_CHARS = "0123456789ABCDEF".toCharArray()
    }

    init {
        prefs = context?.getSharedPreferences(
            BP_PREFS,
            Context.MODE_PRIVATE
        )!!
    }
}