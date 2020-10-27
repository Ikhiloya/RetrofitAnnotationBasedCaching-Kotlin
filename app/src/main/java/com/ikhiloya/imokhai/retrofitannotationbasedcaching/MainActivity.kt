package com.ikhiloya.imokhai.retrofitannotationbasedcaching

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.adapter.PaymentAdapter
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.injector.Injector
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.model.PaymentType
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.service.PaymentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var paymentAdapter: PaymentAdapter
    private lateinit var paymentTypes: MutableList<PaymentType>
    private lateinit var paymentService: PaymentService
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        paymentService = Injector.providePaymentService()!!
        paymentTypes = ArrayList()

        val paymentRcv = findViewById<RecyclerView>(R.id.payment_rcv)
        progressBar = findViewById(R.id.progress_bar)

        paymentAdapter = PaymentAdapter(paymentTypes)
        val linearLayoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            paymentRcv.context,
            linearLayoutManager.orientation
        )

        paymentRcv.layoutManager = linearLayoutManager
        paymentRcv.addItemDecoration(dividerItemDecoration)
        paymentRcv.adapter = paymentAdapter


        if (PaymentApp.instance!!.isNetworkConnected()) {
            getPaymentTypes()
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.network_unavailable_msg),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.refresh_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        if (item.itemId == R.id.refresh) {
            getPaymentTypes()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getPaymentTypes() {
        progressBar.visibility = View.VISIBLE
        paymentService.getPaymentTypesWithHeaders()
            .enqueue(object : Callback<MutableList<PaymentType>> {
                override fun onFailure(call: Call<MutableList<PaymentType>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Timber.e(t, getString(R.string.error_occurred))
                }

                override fun onResponse(
                    call: Call<MutableList<PaymentType>>,
                    response: Response<MutableList<PaymentType>>
                ) {

                    if (response.isSuccessful && response.body() != null
                        && response.body()!!.isNotEmpty()
                    ) {
                        if (response.raw().networkResponse != null) {
                            Timber.i("onResponse: response is from NETWORK...")
                            Toast.makeText(
                                this@MainActivity,
                                "onResponse: response is from NETWORK...",
                                Toast.LENGTH_LONG
                            ).show()
                        } else if (response.raw().cacheResponse != null
                            && response.raw().networkResponse == null
                        ) {
                            Timber.i("onResponse: response is from CACHE...")
                            Toast.makeText(
                                this@MainActivity,
                                "onResponse: response is from CACHE...",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        val data = response.body()
                        Timber.i("Data from network%s", data!!.toString())
                        progressBar.visibility = View.GONE
                        paymentTypes.clear()
                        paymentTypes.addAll(data)
                        paymentAdapter.notifyDataSetChanged()
                    }

                }
            })
    }
}
