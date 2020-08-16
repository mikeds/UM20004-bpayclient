package com.uxi.bambupay.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uxi.bambupay.R
import com.uxi.bambupay.model.ubp.Bank
import kotlinx.android.synthetic.main.item_bank_partner.view.*

/**
 * Created by Eraño Payawal on 8/15/20.
 * hunterxer31@gmail.com
 */
class BankAdapter(private val context: Context, private var list: MutableList<Bank>?) :
    RecyclerView.Adapter<BankAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, private val context: Context?) : RecyclerView.ViewHolder(itemView) {
        private var textTitle: TextView = itemView.text_bank_name

        fun bind(item: Bank?) {
            item?.let {
                textTitle.text = it?.bank
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_bank_partner, parent, false)
        return ViewHolder(itemView, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun getItem(position: Int) : Bank? {
        return list?.get(position)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}