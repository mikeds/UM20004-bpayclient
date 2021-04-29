package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.uxi.bambupay.R
import com.uxi.bambupay.databinding.ActivitySuccessRegistrationBinding
import com.uxi.bambupay.view.ext.viewBinding

/**
 * Created by Eraño Payawal on 4/29/21.
 * hunterxer31@gmail.com
 */
class SuccessRegistrationActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivitySuccessRegistrationBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.appToolbar.toolbar)
        binding.appToolbar.toolbar.elevation = 0f
        binding.appToolbar.tvToolbarTitle.visibility = View.GONE
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }

    override fun onBackPressed() {
        showLoginScreen()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                showLoginScreen()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_down)
    }

    private fun showLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        finish()
    }

}