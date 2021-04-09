package com.uxi.bambupay.model.registration

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eraño Payawal on 4/8/21.
 * hunterxer31@gmail.com
 */
class Fund : Serializable {

    @SerializedName("sof_id")
    var id: String? = null

    @SerializedName("sof_name")
    var name: String? = null

}