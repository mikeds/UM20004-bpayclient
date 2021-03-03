package com.uxi.bambupay.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.uxi.bambupay.R
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.utils.RecyclerItemClickListener
import com.uxi.bambupay.view.activity.*
import com.uxi.bambupay.view.adapter.RecentTransactionsAdapter
import com.uxi.bambupay.viewmodel.HomeViewModel
import com.uxi.bambupay.viewmodel.TransactionViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), View.OnClickListener {

    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val homeViewModel by viewModel<HomeViewModel>()
    private val transactionViewModel by viewModel<TransactionViewModel>()

    override fun getLayoutId() = R.layout.fragment_home

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAdapter()
        observeViewModel()
        events()

        if (context is MainActivity) {
            (context as MainActivity).setToolbarBgColor(R.color.colorPrimary)
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.subscribeUserBalance()
        transactionViewModel.subscribeRecentTransactions()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_cash_in -> {
                val intent = Intent(activity, SelectCashInActivity::class.java)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
            R.id.btn_send_money -> {
//                val intent = Intent(activity, OtpActivity::class.java)
                // for testing only
                val intent = Intent(activity, SendMoneyActivity::class.java)
                intent.putExtra(Constants.SCREEN_FROM, Constants.SEND_MONEY_SCREEN)
                // note: for testing purposes only
                // intent.putExtra(Constants.MOBILE_NUMBER, "09266959071")
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
            R.id.btn_cash_out -> {
//                val intent = Intent(activity, OtpActivity::class.java)
                // For testing only
                val intent = Intent(activity, SelectBankActivity::class.java)
                intent.putExtra(Constants.SCREEN_FROM, Constants.CASH_OUT_SCREEN)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
            R.id.btn_view_all -> {
                val intent = Intent(activity, TransactionHistoryActivity::class.java)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }
            R.id.btn_pay_qr -> {
                val intent = Intent(activity, SelectPayQRActivity::class.java)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
            }

        }
    }

    private fun setupAdapter() {
        recycler_view_recent?.apply {
            val adapter = activity?.let { RecentTransactionsAdapter(it, transactionViewModel.filterRecentTransactions()) }
            recycler_view_recent?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL ,false)
            recycler_view_recent?.adapter = adapter
            val decorator = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
            decorator.setDrawable(activity?.let { ContextCompat.getDrawable(it, R.drawable.divider) }!!)
            addItemDecoration(decorator)
            addOnItemTouchListener(
                RecyclerItemClickListener(activity, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val item = adapter?.getItem(position)
                        val intent = Intent(context, TransactionDetailsActivity::class.java)
                        val transactionId = item?.id
                        intent.putExtra("transactionId", transactionId)
                        intent.putExtra("history_type", "RECENT")
                        startActivity(intent)
                        activity?.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                    }
                })
            )
        }

    }

    private fun observeViewModel() {
        homeViewModel.rxUIBalance()

        homeViewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                container_balance.visibility = View.GONE
                shimmer_view_container.visibility = View.VISIBLE
                shimmer_view_container.startShimmer()
            } else {
                container_balance.visibility = View.VISIBLE
                shimmer_view_container.visibility = View.GONE
                shimmer_view_container.stopShimmer()
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
                 val balance = "₱$textBalance"
                 txt_current_balance.visibility = View.VISIBLE
                 txt_current_balance.text = balance
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

        btn_cash_in.setOnClickListener(this)
        btn_send_money.setOnClickListener(this)
        btn_cash_out.setOnClickListener(this)
        btn_view_all.setOnClickListener(this)
        btn_pay_qr.setOnClickListener(this)
    }

}