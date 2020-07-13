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