package com.uxi.bambupay.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.uxi.bambupay.R
import com.uxi.bambupay.model.ubp.Bank
import com.uxi.bambupay.view.adapter.BankAdapter
import com.uxi.bambupay.viewmodel.InstaPayViewModel
import kotlinx.android.synthetic.main.activity_select_bank.*
import kotlinx.android.synthetic.main.app_toolbar.*

/**
 * Created by Eraño Payawal on 8/15/20.
 * hunterxer31@gmail.com
 */
class SelectBankActivity : BaseActivity() {

    private val instaPayViewModel by viewModel<InstaPayViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        observeViewModel()
    }

    override fun getLayoutId() = R.layout.activity_select_bank

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_close, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_close -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_down)
    }

    override fun onBackPressed() {
        finish()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        tv_toolbar_title?.text = getString(R.string.select_bank)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun observeViewModel() {
        instaPayViewModel.subscribeInstaPayBanks()

        instaPayViewModel.banksData.observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                val appsList: MutableList<Bank> = list
                appsList.sortBy { it.bank }
                setBankAdapter(list)
            }
        })
    }

    private fun setBankAdapter(list: MutableList<Bank>) {
        val bankAdapter = BankAdapter(this, list)
        rv_bank.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            isNestedScrollingEnabled = false
            layoutManager = linearLayoutManager
            adapter = bankAdapter
        }
    }

}