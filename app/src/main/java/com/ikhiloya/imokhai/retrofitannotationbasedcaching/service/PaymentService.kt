package com.ikhiloya.imokhai.retrofitannotationbasedcaching.service

import com.ikhiloya.imokhai.retrofitannotationbasedcaching.annotation.Cacheable
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.model.PaymentType
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.util.Constant.Companion.PAYMENT_TYPES
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface PaymentService {
    @Cacheable
    @GET(PAYMENT_TYPES)
    fun getPaymentTypes(): Call<MutableList<PaymentType>>


    @GET(PAYMENT_TYPES)
    @Headers("Cacheable: true")
    fun getPaymentTypesWithHeaders(): Call<MutableList<PaymentType>>
}
