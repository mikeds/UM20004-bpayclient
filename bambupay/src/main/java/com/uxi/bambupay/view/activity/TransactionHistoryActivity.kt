package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.uxi.bambupay.R
import com.uxi.bambupay.utils.RecyclerItemClickListener
import com.uxi.bambupay.view.adapter.TransactionsHistoryAdapter
import com.uxi.bambupay.viewmodel.TransactionViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.activity_transaction_history.*
import kotlinx.android.synthetic.main.app_toolbar.*

class TransactionHistoryActivity : BaseActivity() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val transactionViewModel by viewModel<TransactionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        setupAdapter()
        observeViewModel()
        events()
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
        /*val adapter = RecentTransactionsAdapter(this@TransactionHistoryActivity, data)
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
        )*/

        val adapter = TransactionsHistoryAdapter(this@TransactionHistoryActivity, transactionViewModel.filterTransactions())
        recycler_view_history?.layoutManager = LinearLayoutManager(this@TransactionHistoryActivity, LinearLayoutManager.VERTICAL ,false)
        recycler_view_history?.adapter = adapter
        val decorator = DividerItemDecoration(this@TransactionHistoryActivity, LinearLayoutManager.VERTICAL)
        ContextCompat.getDrawable(this@TransactionHistoryActivity, R.drawable.divider)?.let { decorator.setDrawable(it) }
        recycler_view_history?.addItemDecoration(decorator)
        recycler_view_history?.addItemDecoration(decorator)
        recycler_view_history?.addOnItemTouchListener(
            RecyclerItemClickListener(this@TransactionHistoryActivity, object : RecyclerItemClickListener.OnItemClickListener {

                override fun onItemClick(view: View?, position: Int) {
                    val item = adapter.getItem(position)
                    val intent = Intent(this@TransactionHistoryActivity, TransactionDetailsActivity::class.java)
//                    intent.putExtra("transaction", item)
                    startActivity(intent)
                    overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                }
            })
        )
    }

    private fun observeViewModel() {
        transactionViewModel.subscribeTransactions()

        userTokenModel.isTokenRefresh.observe(this, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
                transactionViewModel.subscribeTransactions()
            }
        })

        transactionViewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (!isSuccess) {
                // call token refresher
                userTokenModel.subscribeToken()
            }
        })
    }
}