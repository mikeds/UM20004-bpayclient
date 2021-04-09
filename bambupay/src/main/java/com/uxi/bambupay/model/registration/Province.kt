package com.uxi.bambupay.model.registration

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eraño Payawal on 9/20/20.
 * hunterxer31@gmail.com
 */
class Province : Serializable {

    constructor(provinceId: String?, provinceName: String?) {
        this.provinceId = provinceId
        this.provinceName = provinceName
    }

    @SerializedName("province_id")
    var provinceId: String? = null

    @SerializedName("province_name")
    var provinceName: String? = null

}