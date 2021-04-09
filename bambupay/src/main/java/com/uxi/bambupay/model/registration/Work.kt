package com.uxi.bambupay.model.registration

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by Eraño Payawal on 4/7/21.
 * hunterxer31@gmail.com
 */
class Work : Serializable {

    @SerializedName("now_id")
    var id: String? = null

    @SerializedName("now_name")
    var name: String? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that: Work = o as Work
        return id == that.id &&
                name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name)
    }
}