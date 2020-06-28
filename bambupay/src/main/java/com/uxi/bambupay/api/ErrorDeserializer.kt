package com.uxi.bambupay.api

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.uxi.bambupay.model.User
import java.lang.reflect.Type

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
class ErrorDeserializer : JsonDeserializer<User> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): User {
        val user: User = Gson().fromJson(json, typeOfT)
        return user
    }
}