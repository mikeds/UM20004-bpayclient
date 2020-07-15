package com.uxi.bambupay.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uxi.bambupay.R
import com.uxi.bambupay.model.Transaction
import com.uxi.bambupay.utils.Constants.Companion.CASH_IN
import com.uxi.bambupay.utils.Constants.Companion.CASH_OUT
import com.uxi.bambupay.utils.Constants.Companion.SEND_MONEY
import com.uxi.bambupay.utils.convertTimeToDate
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_transaction.view.*


class TransactionsHistoryAdapter(
    private val context: Context,
    collection: OrderedRealmCollection<Transaction>
) :
    RealmRecyclerViewAdapter<Transaction, TransactionsHistoryAdapter.ViewHolder>(collection, true) {

    class ViewHolder(itemView: View, private val context: Context?) : RecyclerView.ViewHolder(itemView) {

        private var txtTransactionType: TextView = itemView.txt_transaction_type
        private var txtDate: TextView = itemView.txt_date
        private var txtAmount: TextView = itemView.txt_amount

        fun bind(item: Transaction?) {
            item?.let {
                val transactionType = it.type
                when {
                    transactionType.equals(SEND_MONEY) -> {
                        txtTransactionType.text = context?.getString(R.string.send_money)
                    }
                    transactionType.equals(CASH_IN) -> {
                        txtTransactionType.text = context?.getString(R.string.cash_in)
                    }
                    transactionType.equals(CASH_OUT) -> {
                        txtTransactionType.text = context?.getString(R.string.cash_out)
                    }
                }
                txtAmount.text = it.amount
                val dateTime = convertTimeToDate(it.date)
                txtDate.text = dateTime
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(itemView, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

}