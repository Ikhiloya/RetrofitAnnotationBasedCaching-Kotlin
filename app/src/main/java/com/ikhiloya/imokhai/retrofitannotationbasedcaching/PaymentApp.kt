package com.ikhiloya.imokhai.retrofitannotationbasedcaching

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.interceptor.NetworkInterceptor
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.interceptor.OfflineCacheInterceptor
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.interceptor.OfflineCacheInterceptorWithHeader
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.service.PaymentService
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.util.Constant.Companion.BASE_URL
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import timber.log.Timber.DebugTree

class PaymentApp : Application() {
    private lateinit var paymentService: PaymentService


    init {
        instance = this
    }


    companion object {
        var instance: PaymentApp? = null

        fun applicationContext(): PaymentApp {
            return instance as PaymentApp
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //Gson Builder
        val gsonBuilder = GsonBuilder()
        gsonBuilder.excludeFieldsWithoutExposeAnnotation()
        val gson = gsonBuilder.create()
        Timber.plant(DebugTree())


        // HttpLoggingInterceptor
        val httpLoggingInterceptor = HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { message: String? -> Timber.i(message) }
        )

        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val networkInterceptor = NetworkInterceptor()
        val offlineCacheInterceptor = OfflineCacheInterceptor()
        val offlineCacheInterceptorWithHeader = OfflineCacheInterceptorWithHeader()


        // OkHttpClient
        val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
            .cache(cache())
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(networkInterceptor) // only used when network is on
            .addInterceptor(offlineCacheInterceptor)
            .build()


        //Retrofit
        val retrofit: Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        paymentService = retrofit.create(PaymentService::class.java)
    }

    fun getPaymentService(): PaymentService? {
        return paymentService
    }


    private fun cache(): Cache? {
        val cacheSize = 5 * 1024 * 1024.toLong()
        return Cache(instance!!.applicationContext.cacheDir, cacheSize)
    }


    fun isNetworkConnected(): Boolean {
        val cm: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
    }
}