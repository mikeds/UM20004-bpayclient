package com.uxi.bambupay.utils

import java.text.SimpleDateFormat

fun getDateTimeFormat(unix: Long?) : String {
    var dateTime = ""
    val sdf = SimpleDateFormat("MMMM dd, yyyy | hh:mm aa")
    val date = unix?.times(1000)?.let { java.util.Date(it) }
    return sdf.format(date).toString()
}