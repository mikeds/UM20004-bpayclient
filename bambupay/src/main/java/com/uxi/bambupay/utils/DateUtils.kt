package com.uxi.bambupay.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun getDateTimeFormat(unix: Long?) : String {
    var dateTime = ""
    val sdf = SimpleDateFormat("MMMM dd, yyyy | hh:mm aa")
    val date = unix?.times(1000)?.let { java.util.Date(it) }
    return sdf.format(date).toString()
}

fun convertTimeToDate(isoTime: String?): String? {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var convertedDate: Date? = null
    var formattedDate: String? = null
    try {
        convertedDate = sdf.parse(isoTime)
        formattedDate = SimpleDateFormat("MMMM dd, yyyy | hh:mm aa").format(convertedDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formattedDate
}

fun convertDoB(date: String): String? {
    val sdfOrigin = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    val sdfOutput = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    var outputDate: String? = null
    try {
        val dateSource = sdfOrigin.parse(date)
        outputDate = sdfOutput.format(dateSource)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return outputDate
}

fun formatDate(date: Date): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy")
    return sdf.format(date).toString()
}