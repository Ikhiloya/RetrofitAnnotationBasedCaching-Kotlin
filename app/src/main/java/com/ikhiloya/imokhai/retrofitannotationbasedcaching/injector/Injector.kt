package com.ikhiloya.imokhai.retrofitannotationbasedcaching.injector

import com.ikhiloya.imokhai.retrofitannotationbasedcaching.PaymentApp
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.service.PaymentService

class Injector {
    companion object {
        fun providePaymentService(): PaymentService? {
            return PaymentApp.instance!!.getPaymentService()
        }
    }
}