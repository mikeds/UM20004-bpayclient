package com.uxi.bambupay.view.activity

import android.graphics.Bitmap
import android.net.UrlQuerySanitizer
import android.os.Bundle
import android.view.MenuItem
import android.webkit.*
import com.uxi.bambupay.BuildConfig
import com.uxi.bambupay.R
import com.uxi.bambupay.utils.Constants
import kotlinx.android.synthetic.main.activity_paynamics.*
import kotlinx.android.synthetic.main.app_toolbar.*
import timber.log.Timber
import java.net.URI

/**
 * Created by Eraño Payawal on 1/30/21.
 * hunterxer31@gmail.com
 */
class CashInPaynamicsActivity : BaseActivity() {

    private val redirectUrl by lazy {
        intent?.getStringExtra(Constants.CASH_IN_REDIRECT_URL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        initViews()
    }

    override fun getLayoutId() = R.layout.activity_paynamics

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
        tv_toolbar_title?.text = getString(R.string.cash_in)
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
        web_view.addJavascriptInterface(this, "androidObj")
        redirectUrl?.let {
            web_view.loadUrl(it)
        }
    }

    @JavascriptInterface
    fun showHTML(obj: String?) {
        Timber.tag("DEBUG").e("response data:: $obj")
    }

    private fun parseUrl(url: String?) {
        url?.let {

            if (it.contains(BuildConfig.API_BASE_URL)) {
                Timber.tag("DEBUG").e("passed response")
                /*val callbackUrl = BuildConfig.API_BASE_URL + "callback/paynamics/response"
                if (it == callbackUrl) {
                    Timber.tag("DEBUG").e("Success CashIn")
                    showMain()
                }*/

                val uri = URI(it)
                val scheme = uri.scheme
                val host = uri.host // HOST

                if (!host.isNullOrEmpty() && host == BuildConfig.API_BASE_URL) {
                    val query = uri.rawQuery
                    val urlQuerySanitizer = UrlQuerySanitizer(url)
                    val success = urlQuerySanitizer.getValue("success")

                    Timber.tag("DEBUG").e("Scheme:: $scheme")
                    Timber.tag("DEBUG").e("Host:: $host")
                    Timber.tag("DEBUG").e("Query:: $query")
                    Timber.tag("DEBUG").e("success:: $success")

                    if (success == "true") {
                        showMain()
                    }
                }
            }
        }
    }

}