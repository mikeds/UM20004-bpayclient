package com.uxi.bambupay.view.ext

/**
 * Created by Eraño Payawal on 4/7/21.
 * hunterxer31@gmail.com
 */

inline fun <reified T> List<*>.asListOfType(): List<T>? =
    if (all { it is T })
        @Suppress("UNCHECKED_CAST")
        this as List<T> else
        null


inline fun <reified T> List<*>.typeOf() : List<T> {
    val retList = mutableListOf<T>()
    this.forEach {
        if (it is T) {
            retList.add(it)
        }
    }
    return retList
}