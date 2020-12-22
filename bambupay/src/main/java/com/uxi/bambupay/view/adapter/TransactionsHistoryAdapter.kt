package com.uxi.bambupay.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.uxi.bambupay.R
import com.uxi.bambupay.model.Transaction
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.utils.Constants.Companion.CASH_IN
import com.uxi.bambupay.utils.Constants.Companion.CASH_IN_PAYNAMICS
import com.uxi.bambupay.utils.Constants.Companion.CASH_OUT
import com.uxi.bambupay.utils.Constants.Companion.CREATE_SCAN_QR
import com.uxi.bambupay.utils.Constants.Companion.QUICK_PAY_QR
import com.uxi.bambupay.utils.Constants.Companion.SCAN_PAY_QR
import com.uxi.bambupay.utils.Constants.Companion.SEND_MONEY
import com.uxi.bambupay.utils.Utils
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

        private var btn_item_click: RelativeLayout = itemView.btn_item_click
        private var txtTransactionType: TextView = itemView.txt_transaction_type
        private var txtDate: TextView = itemView.txt_date
        private var txtAmount: TextView = itemView.txt_amount
        private var utils: Utils? = null

        init {
            utils = Utils(context)
        }

        fun bind(item: Transaction?) {
            item?.let {
                when (it.transactionType) {
                    SEND_MONEY -> {
                        txtTransactionType.text = context?.getString(R.string.send_money)
                        if (it.balanceType == Constants.DEBIT) {
                            context?.let {
                                txtAmount.setTextColor((ContextCompat.getColor(context, R.color.red)))
                            }
                        }
                    }
                    CASH_IN, CASH_IN_PAYNAMICS -> {
                        txtTransactionType.text = context?.getString(R.string.cash_in)
                        context?.let {
                            txtAmount.setTextColor((ContextCompat.getColor(context, R.color.light_green)))
                        }
                    }
                    CASH_OUT -> txtTransactionType.text = context?.getString(R.string.cash_out)
                    SCAN_PAY_QR -> txtTransactionType.text = context?.getString(R.string.scan_pay)
                    CREATE_SCAN_QR -> txtTransactionType.text = context?.getString(R.string.create_scan_qr)
                    QUICK_PAY_QR -> txtTransactionType.text = context?.getString(R.string.quick_qr)
                }
                val transactionAmount = it.amount?.let { it1 -> utils?.currencyFormat(it1) }
                txtAmount.text = context?.getString(R.string.ph_currency, transactionAmount)
                val dateTime = convertTimeToDate(it.dateCreated)
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