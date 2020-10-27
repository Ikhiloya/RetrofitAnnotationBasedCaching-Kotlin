package com.ikhiloya.imokhai.retrofitannotationbasedcaching.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.R
import com.ikhiloya.imokhai.retrofitannotationbasedcaching.model.PaymentType

class PaymentAdapter(private var paymentTypes: MutableList<PaymentType>) : RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.payment_item, parent, false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return if (paymentTypes.isNotEmpty()) paymentTypes.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(paymentTypes[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val paymentNameTxt: TextView? = itemView.findViewById(R.id.payment_name_txt)
        fun bind(paymentType: PaymentType) {
            paymentNameTxt!!.text = paymentType.name
        }
    }
}