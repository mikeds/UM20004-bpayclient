package com.uxi.bambupay.utils

//class StringUtils {

    fun isChinese(str: String? = ""): Boolean {
        return try {
            val pattern = "^[\\p{Han}]+$"
            val firstLetter = str!!.substring(0, 1)
            firstLetter.matches(pattern.toRegex())
        } catch (e: StringIndexOutOfBoundsException) {
            false
        } catch (e: KotlinNullPointerException) {
            false
        }
    }

    fun getNameInitial(firstName: String = "", lastName: String = ""): String {
        var initial = ""
        if (isChinese(lastName)) {
            // for Chinese name
            initial = lastName
        } else {
            // for English name
            if (!firstName.isBlank()) {
                initial += firstName[0].toString()
            }
            if (!lastName.isBlank()) {
                initial += lastName[0].toString()
            }
        }
        return initial
    }

    fun getNameInitial(fullName: String?) : String {
        if (!fullName.isNullOrBlank() || !fullName.isNullOrEmpty()) {
            return fullName.replace("^\\s*([a-zA-Z]).*\\s+([a-zA-Z])\\S+\$".toRegex(), "$fullName")
        }
        return ""
    }

fun buildName(firstName: String? = "", lastName: String? = ""): String {
    var name: String
    if (isChinese(lastName)) {
        // for Chinese name
        name = lastName + firstName
    } else {
        //for English name
        name = "$firstName $lastName"
    }
    return name.trim { it <= ' ' }
}

//}