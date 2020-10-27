package com.ikhiloya.imokhai.retrofitannotationbasedcaching

import android.content.res.Resources.NotFoundException
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.annotation.Cacheable
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.interceptor.NetworkInterceptor
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.interceptor.OfflineCacheInterceptor
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.service.PaymentService
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.util.Constant.Companion.BASE_URL
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.util.Constant.Companion.PAYMENT_TYPES
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert
import org.junit.Test
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException

class CustomAnnotationUnitTest {
    /**
     * Run when OfflineCacheInterceptor is used
     */
    @Test(expected = NotFoundException::class)
    @Throws(Exception::class)
    fun testIfCustomAnnotationIsPresent() {
        val httpLoggingInterceptor = HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { message: String? -> Timber.i(message) }
        )
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val networkInterceptor = NetworkInterceptor()

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .addInterceptor(
                object : OfflineCacheInterceptor() {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val request: Request = chain.request()
                        /* tes the request method */
                        Assert.assertEquals("GET", request.method)
                        val url = request.url
                        /* Test the url */
                        Assert.assertEquals(
                            BASE_URL + PAYMENT_TYPES,
                            url.toString()
                        )

                        /* get the Cacheable annotation from the request */
                        val invocation = request.tag(
                            Invocation::class.java
                        )
                        Assert.assertNotNull(invocation)
                        val annotation: Cacheable? =
                            invocation!!.method().getAnnotation(Cacheable::class.java)
                        Assert.assertNotNull(annotation)
                        Assert.assertEquals("Cacheable", annotation!!.annotationClass.simpleName)

                        // The following just ends the test with an expected exception.
                        // You could construct a valid Response and return that instead
                        // Do not return chain.proceed(), because then your unit test may become
                        // subject to the whims of the network
                        throw NotFoundException()
                    }
                })
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val paymentService: PaymentService =
            retrofit.create(PaymentService::class.java)
        paymentService.getPaymentTypes().execute()
    }


    /**
     * Run when OfflineCacheInterceptorWithHeader is used
     */
    @Test(expected = NotFoundException::class)
    @Throws(Exception::class)
    fun testIfCustomHeaderIsPresent() {
        val httpLoggingInterceptor = HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { message: String? -> Timber.i(message) }
        )
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val networkInterceptor = NetworkInterceptor()

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .addInterceptor(
                object : OfflineCacheInterceptor() {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val request: Request = chain.request()
                        /* tes the request method */
                        Assert.assertEquals("GET", request.method)
                        val url = request.url
                        /* Test the url */
                        Assert.assertEquals(
                            BASE_URL + PAYMENT_TYPES,
                            url.toString()
                        )

                        /* get the RequiresCaching annotation from the request */
                        val header = request.headers["Cacheable"]

                        Assert.assertNotNull(header)
                        Assert.assertEquals("true", header)

                        // The following just ends the test with an expected exception.
                        // You could construct a valid Response and return that instead
                        // Do not return chain.proceed(), because then your unit test may become
                        // subject to the whims of the network
                        throw NotFoundException()
                    }
                })
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val paymentService: PaymentService =
            retrofit.create(PaymentService::class.java)
        paymentService.getPaymentTypesWithHeaders().execute()
    }

}