package com.uxi.bambupay.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.amulyakhare.textdrawable.TextDrawable
import com.uxi.bambupay.MainActivity
import com.uxi.bambupay.R
import com.uxi.bambupay.ui.activity.CashOutActivity
import com.uxi.bambupay.ui.activity.TransactActivity
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
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
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
    }

}