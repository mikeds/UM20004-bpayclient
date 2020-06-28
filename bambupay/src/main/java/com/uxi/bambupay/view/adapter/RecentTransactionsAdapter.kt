package com.uxi.bambupay.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uxi.bambupay.R
import com.uxi.bambupay.model.RecentTransaction
import com.uxi.bambupay.utils.Constants.Companion.CASH_IN
import com.uxi.bambupay.utils.Constants.Companion.SEND_MONEY
import com.uxi.bambupay.utils.getDateTimeFormat
import kotlinx.android.synthetic.main.item_transaction.view.*


class RecentTransactionsAdapter(
    private val context: Context,
    private var arrayList: ArrayList<RecentTransaction>?
) :
    RecyclerView.Adapter<RecentTransactionsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, private val context: Context?) : RecyclerView.ViewHolder(itemView) {

        var txtTransactionType: TextView = itemView.txt_transaction_type
        var txtDate: TextView = itemView.txt_date
        var txtAmount: TextView = itemView.txt_amount

        fun bind(item: RecentTransaction?) {
            item?.let {
                val transactionType = it.transactionType
                if (transactionType.equals(SEND_MONEY)) {
                    txtTransactionType.text = context?.getString(R.string.send_money)
                } else if (transactionType.equals(CASH_IN)) {
                    txtTransactionType.text = context?.getString(R.string.cash_in)
                }
                txtAmount.text = it.transactionAmount
                val dateTime = getDateTimeFormat(it.createdAt)
                txtDate.text = dateTime
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(itemView, context)
    }

    override fun getItemCount(): Int {
        return arrayList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        arrayList?.get(position)?.let { holder.bind(it) }
    }

    fun getItem(position: Int) : RecentTransaction? {
        return arrayList?.get(position)
    }
}