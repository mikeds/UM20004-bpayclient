package com.uxi.bambupay.view.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.uxi.bambupay.R
import kotlinx.android.synthetic.main.activity_login_ubp.*
import kotlinx.android.synthetic.main.app_toolbar.*
import timber.log.Timber

/**
 * Created by Eraño Payawal on 8/16/20.
 * hunterxer31@gmail.com
 */
class LoginUbpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_ubp)
        setupToolbar()
        initViews()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        tv_toolbar_title?.text = getString(R.string.union_bank_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private var loadingFinished = true
    private var redirect = false

    private fun initViews() {
        web_view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (!loadingFinished) {
                    redirect = true
                }
                loadingFinished = false
                view?.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loadingFinished = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (!redirect) {
                    loadingFinished = true

                    Timber.tag("DEBUG").e("onPageFinished $url")


                    /*if (url?.contains("success")) {
                        if (dialog != null) {
                            dialog.dismiss();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable () {
                                @Override
                                public void run() {
                                    showSuccessAlertMessage();
                                }
                            }, 1000);

                        }
                    } else if (url.contains("failed")) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }*/

                } else {
                    redirect = false
                }
            }
        }

        val webSettings: WebSettings = web_view.settings
        webSettings.javaScriptEnabled = true
//        web_view.loadUrl("https://api-uat.unionbankph.com/partners/sb/customers/v1/oauth2/authorize?client_id=854b7778-c9d3-4b3e-9fd5-21c828f7df39&response_type=code&scope=payments&redirect_uri=https://api.resolveitthrough.us&type=single&partnerId=5dff2cdf-ef15-48fb-a87b-375ebff415bb")
        web_view.loadUrl("https://api-uat.unionbankph.com/partners/sb/convergent/v1/oauth2/authorize?response_type=code&client_id=854b7778-c9d3-4b3e-9fd5-21c828f7df39&redirect_uri=https://api.resolveitthrough.us&scope=payments")

    }

}