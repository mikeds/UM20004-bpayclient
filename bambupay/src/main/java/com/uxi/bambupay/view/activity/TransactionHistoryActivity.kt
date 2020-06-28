package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.uxi.bambupay.R
import com.uxi.bambupay.model.RecentTransaction
import com.uxi.bambupay.view.adapter.RecentTransactionsAdapter
import com.uxi.bambupay.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.activity_transaction_history.*
import kotlinx.android.synthetic.main.app_toolbar.*

class TransactionHistoryActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        events()
        setupAdapter()
    }

    override fun getLayoutId() = R.layout.activity_transaction_history

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        tv_toolbar_title?.text = getString(R.string.transaction_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun events() {

    }

    private fun setupAdapter() {
        val data = arrayListOf<RecentTransaction>()
        data.add(RecentTransaction("cash_in", 1586931717, "PHP 40,000", "+63911366989", "salary", "BP4DC56893GH"))
        data.add(RecentTransaction("send_money", 1586034532, "PHP 650", "+63911366989", "payment", "BP4DB56893GH"))
        data.add(RecentTransaction("send_money", 1586034532, "PHP 2,000", "+63911366789", "payment", "BP4DA56893GH"))
        data.add(RecentTransaction("send_money", 1585559688, "PHP 2,000", "+63911366789", "payment", "BP4DA56893GH"))
        data.add(RecentTransaction("cash_in", 1585559688, "PHP 20,000", "+63912366789", "budget", "BP4DA56793FH"))
        data.add(RecentTransaction("cash_in", 1585250675, "PHP 5,000", "+63912366789", "budget", "BP4DA54893FH"))
        data.add(RecentTransaction("cash_in", 1585111159, "PHP 9,000", "+63912366789", "budget", "BP4DA56893FH"))
        data.add(RecentTransaction("send_money", 1584696907, "PHP -1,000", "+639123456789", "payment", "BP4DA56893FH"))

        val adapter = RecentTransactionsAdapter(this@TransactionHistoryActivity, data)
        recycler_view_history?.layoutManager = LinearLayoutManager(this@TransactionHistoryActivity, LinearLayoutManager.VERTICAL ,false)
        recycler_view_history?.adapter = adapter
        val decorator = DividerItemDecoration(this@TransactionHistoryActivity, LinearLayoutManager.VERTICAL)
        ContextCompat.getDrawable(this@TransactionHistoryActivity, R.drawable.divider)?.let { decorator.setDrawable(it) }
        recycler_view_history?.addItemDecoration(decorator)
        recycler_view_history?.addOnItemTouchListener(
            RecyclerItemClickListener(this@TransactionHistoryActivity, object : RecyclerItemClickListener.OnItemClickListener {

                override fun onItemClick(view: View?, position: Int) {
                    val item = adapter.getItem(position)
                    val intent = Intent(this@TransactionHistoryActivity, TransactionDetailsActivity::class.java)
                    intent.putExtra("transaction", item)
                    startActivity(intent)
                    overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                }
            })
        )
    }
}