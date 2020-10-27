package com.ikhiloya.imokhai.retrofitannotationbasedcaching.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaymentType(
    @SerializedName("id")
    @Expose
    var id: Long,
    @SerializedName("name")
    @Expose
    var name: String
)

