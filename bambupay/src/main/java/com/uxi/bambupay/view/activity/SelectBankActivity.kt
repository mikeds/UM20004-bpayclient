package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.uxi.bambupay.R
import com.uxi.bambupay.model.ubp.Bank
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.utils.RecyclerItemClickListener
import com.uxi.bambupay.view.adapter.BankAdapter
import com.uxi.bambupay.viewmodel.CashOutViewModel
import com.uxi.bambupay.viewmodel.InstaPayViewModel
import kotlinx.android.synthetic.main.activity_select_bank.*
import kotlinx.android.synthetic.main.app_toolbar.*
import timber.log.Timber

/**
 * Created by Eraño Payawal on 8/15/20.
 * hunterxer31@gmail.com
 */
class SelectBankActivity : BaseActivity() {

//    private val instaPayViewModel by viewModel<InstaPayViewModel>()
    private val cashOutViewModel by viewModels<CashOutViewModel> { viewModelFactory }

    private val fromScreen by lazy {
        intent?.getStringExtra(Constants.SCREEN_FROM)
    }

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
//        instaPayViewModel.subscribeInstaPayBanks()
        cashOutViewModel.subscribeGetBanks()

        /*instaPayViewModel.banksData.observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                val appsList: MutableList<Bank> = list
                appsList.sortBy { it.bank }
                setBankAdapter(list)
            }
        })*/

        cashOutViewModel.banksData.observe(this, Observer { list ->
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
            val itemDecorator = DividerItemDecoration(this@SelectBankActivity, DividerItemDecoration.VERTICAL)
            itemDecorator.setDrawable(ContextCompat.getDrawable(this@SelectBankActivity, R.drawable.divider)!!)
            addItemDecoration(itemDecorator)
            addOnItemTouchListener(
                RecyclerItemClickListener(
                    this@SelectBankActivity,
                    object : RecyclerItemClickListener.OnItemClickListener {

                        override fun onItemClick(view: View?, position: Int) {
                            val item = bankAdapter.getItem(position)

                            item?.let {
                                if (!fromScreen.isNullOrEmpty() && fromScreen == Constants.CASH_IN_BANK_SCREEN) {
                                    if (it.bank?.isNotEmpty()!! && it.bank == "UnionBank") {
                                        Timber.tag("DEBUG").e("UnionBank")
                                        val intent = Intent(this@SelectBankActivity, LoginUbpActivity::class.java)
                                        startActivity(intent)
                                        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                                    }
                                } else {
                                    val intent = Intent(this@SelectBankActivity, CashOutActivity::class.java)
                                    intent.putExtra("bank_code", it.code)
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
                                }
                            }
                        }
                    })
            )
        }
    }

}