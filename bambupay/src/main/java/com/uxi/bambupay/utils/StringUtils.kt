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

//}