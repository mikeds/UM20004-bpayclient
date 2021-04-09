package com.uxi.bambupay.model.registration

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eraño Payawal on 4/8/21.
 * hunterxer31@gmail.com
 */
class IDType : Serializable {

    @SerializedName("id_type_id")
    var id: String? = null

    @SerializedName("id_type_name")
    var name: String? = null

}