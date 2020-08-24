package com.uxi.bambupay.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.UrlQuerySanitizer
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.uxi.bambupay.R
import com.uxi.bambupay.viewmodel.InstaPayViewModel
import kotlinx.android.synthetic.main.activity_login_ubp.*
import kotlinx.android.synthetic.main.app_toolbar.*
import timber.log.Timber
import java.net.URI


/**
 * Created by Eraño Payawal on 8/16/20.
 * hunterxer31@gmail.com
 */
class LoginUbpActivity : BaseActivity() {

    private val instaPayViewModel by viewModel<InstaPayViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        initViews()
    }

    override fun getLayoutId() = R.layout.activity_login_ubp

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

                    parseUrl(url)


                } else {
                    redirect = false
                }
            }
        }

        val webSettings: WebSettings = web_view.settings
        webSettings.javaScriptEnabled = true
//        web_view.loadUrl("https://api-uat.unionbankph.com/partners/sb/customers/v1/oauth2/authorize?client_id=854b7778-c9d3-4b3e-9fd5-21c828f7df39&response_type=code&scope=payments&redirect_uri=https://api.resolveitthrough.us&type=single&partnerId=5dff2cdf-ef15-48fb-a87b-375ebff415bb")
//        web_view.loadUrl("https://api-uat.unionbankph.com/partners/sb/convergent/v1/oauth2/authorize?response_type=code&client_id=854b7778-c9d3-4b3e-9fd5-21c828f7df39&redirect_uri=https://api.resolveitthrough.us&scope=payments")

        web_view.loadUrl("https://api-uat.unionbankph.com/partners/sb/customers/v1/oauth2/authorize?client_id=854b7778-c9d3-4b3e-9fd5-21c828f7df39&response_type=code&scope=transfers&redirect_uri=https://dev-api.resolveitthrough.us/customer/callback/token&type=single&partnerId=5dff2cdf-ef15-48fb-a87b-375ebff415bb")
//        web_view.loadUrl("https://dev-api.resolveitthrough.us/customer/callback/token?code=AAKLsjgPXBnPwBS357MW-F1ZUkjpQua7oe2n-u9LBGYTtTS2NUMe366kHLVd75EXauJ45CBCKWazlrRTQsX1HsqCC8kqeVX6sQKxo0VVd-CsWcd9qCpE7KSDGBfrM8fOWjHiY2ttW0prDqmS2VvyjLSMMog3Ioi-Ue5BpmPhNVDcKMyxjhKiZlr8_z8zgjBcqXAcxCD8Tqi10Nrh437qHZIyS_j-xjTW2UaARRNakFLdidg4HHUe0GaJU0d71ccj_yb6zOpszwIKm7kTZS1mcNrnu3vJWRo8mLGdAKlR25y5OxUJBRpY_KjrhbamViQtxnpY4yYTrLzZlixzmsrMcTt0IeW84qnabyxXB2Ybfr8x3g")

    }

    private fun parseUrl(url: String?) {
        url?.let {
            val uri = URI(it)
            val scheme = uri.scheme
            val host = uri.host // HOST

            if (!host.isNullOrEmpty() && host == "dev-api.resolveitthrough.us") {
                val query = uri.rawQuery
                val urlQuerySanitizer = UrlQuerySanitizer(url)
                val code = urlQuerySanitizer.getValue("code")
                instaPayViewModel.saveCode(code)

                val intent = Intent(this@LoginUbpActivity, CashInBankActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)

//                Timber.tag("DEBUG").e("Scheme:: $scheme")
//                Timber.tag("DEBUG").e("Host:: $host")
//                Timber.tag("DEBUG").e("Query:: $query")
//                Timber.tag("DEBUG").e("Code:: $code")

            }
        }
    }

}