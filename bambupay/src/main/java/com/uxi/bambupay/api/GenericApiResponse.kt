package com.uxi.bambupay.api

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
open class GenericApiResponse<T> {

    var error: Boolean? = null

    @SerializedName("error_description")
    var errorMessage: String? = null

    @SerializedName("message")
    var successMessage: String? = null

    var response: T? = null

    companion object {
        fun <T> create(body: String): GenericApiResponse<T> {
            val collectionType = object : TypeToken<GenericApiResponse<T>>() {}.type
            return Gson().fromJson(body, collectionType)
        }
    }

}