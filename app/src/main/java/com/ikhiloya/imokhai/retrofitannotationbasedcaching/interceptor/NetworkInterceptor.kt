package com.ikhiloya.imokhai.retrofitannotationbasedcaching.interceptor

import com.ikhiloya.imokhai.retrofitannotationbasedcaching.util.Constant.Companion.HEADER_CACHE_CONTROL
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.util.Constant.Companion.HEADER_PRAGMA
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit

class NetworkInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Timber.d("network interceptor: called.")

        val response = chain.proceed(chain.request())

        val cacheControl = CacheControl.Builder()
            .maxAge(20, TimeUnit.SECONDS)
            .build()

        return response.newBuilder()
            .removeHeader(HEADER_PRAGMA)
            .removeHeader(HEADER_CACHE_CONTROL)
            .header(HEADER_CACHE_CONTROL, cacheControl.toString())
            .build()    }
}