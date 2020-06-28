package com.uxi.bambupay.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
class Utils constructor(private val context: Context?) {
    private val prefs: SharedPreferences

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


    companion object {
        const val BP_PREFS = "bp_prefs"
        const val TOKEN = "token"
        const val TOKEN_EXPIRY_DATE = "token_expiry_date"
        const val IS_TOKEN_EXPIRED = "is_token_expired"
        const val IS_LOGGED_IN = "is_logged_in"
        const val HAS_SAW_TUTORIAL = "has_saw_tutorial"
        const val USER_EMAIL = "user_email"
        const val USER_NAME = "user_name"
    }

    init {
        prefs = context?.getSharedPreferences(
            BP_PREFS,
            Context.MODE_PRIVATE
        )!!
    }
}