package com.uxi.bambupay.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.uxi.bambupay.R
import com.uxi.bambupay.view.activity.CashOutActivity
import com.uxi.bambupay.view.activity.TransactActivity
import com.uxi.bambupay.view.activity.TransactionHistoryActivity
import com.uxi.bambupay.view.adapter.RecentTransactionsAdapter
import com.uxi.bambupay.viewmodel.HomeViewModel
import com.uxi.bambupay.viewmodel.TransactionViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val homeViewModel by viewModel<HomeViewModel>()
    private val transactionViewModel by viewModel<TransactionViewModel>()

    override fun getLayoutId() = R.layout.fragment_home

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_pay.setOnClickListener {
            val intent = Intent(activity, TransactActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        btn_cash_out.setOnClickListener {
            val intent = Intent(activity, CashOutActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        btn_view_all.setOnClickListener {
            val intent = Intent(activity, TransactionHistoryActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }

        setupAdapter()
        observeViewModel()
        events()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.subscribeUserBalance()
        transactionViewModel.subscribeRecentTransactions()
    }

    private fun setupAdapter() {
        val adapter = activity?.let { RecentTransactionsAdapter(it, transactionViewModel.filterRecentTransactions()) }
        recycler_view_recent?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL ,false)
        recycler_view_recent?.adapter = adapter
        val decorator = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        decorator.setDrawable(activity?.let { ContextCompat.getDrawable(it, R.drawable.divider) }!!)
        recycler_view_recent?.addItemDecoration(decorator)
    }

    private fun observeViewModel() {
        homeViewModel.rxUIBalance()

        homeViewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {

            } else {
                refresh_layout?.finishRefresh()
                refresh_layout?.finishLoadmore()
            }
        })

        homeViewModel.isSuccess.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (!isSuccess) {
                // call token refresher
                userTokenModel.subscribeToken()
            }
        })

        userTokenModel.isTokenRefresh.observe(viewLifecycleOwner, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
                homeViewModel.subscribeUserBalance()
                transactionViewModel.subscribeRecentTransactions()
            }
        })

        homeViewModel.textBalance.observe(viewLifecycleOwner, Observer { textBalance ->
             if (!textBalance.isNullOrBlank()) {
                 txt_current_balance.text = textBalance
             }
        })

        transactionViewModel.isSuccess.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (!isSuccess) {
                // call token refresher
                userTokenModel.subscribeToken()
            }
        })

    }

    private fun events() {
        refresh_layout?.isEnableRefresh = true
        refresh_layout?.isEnableLoadmore = false

        refresh_layout?.setOnRefreshListener {
            homeViewModel.subscribeUserBalance()
            transactionViewModel.subscribeRecentTransactions()
        }

        refresh_layout?.setOnLoadmoreListener {

        }

        refresh_layout?.finishRefresh()
        refresh_layout?.finishLoadmore()
    }

}