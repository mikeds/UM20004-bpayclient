package com.uxi.bambupay.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.uxi.bambupay.R
import com.uxi.bambupay.model.RecentTransaction
import com.uxi.bambupay.ui.activity.CashOutActivity
import com.uxi.bambupay.ui.activity.TransactActivity
import com.uxi.bambupay.ui.activity.TransactionHistoryActivity
import com.uxi.bambupay.ui.adapter.RecentTransactionsAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })
        return root
    }

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

        val adapter = activity?.let { RecentTransactionsAdapter(it, data) }
        recycler_view_recent?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL ,false)
        recycler_view_recent?.adapter = adapter
        val decorator = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        decorator.setDrawable(activity?.let { ContextCompat.getDrawable(it, R.drawable.divider) }!!)
        recycler_view_recent?.addItemDecoration(decorator)
    }

}