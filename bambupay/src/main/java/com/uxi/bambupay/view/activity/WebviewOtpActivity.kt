package com.uxi.bambupay.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.UrlQuerySanitizer
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.uxi.bambupay.R
import com.uxi.bambupay.utils.Constants
import kotlinx.android.synthetic.main.activity_webview_otp.*
import kotlinx.android.synthetic.main.app_toolbar.*
import timber.log.Timber
import java.net.URI

/**
 * Created by Eraño Payawal on 12/1/20.
 * hunterxer31@gmail.com
 */
class WebviewOtpActivity : AppCompatActivity() {

    private val redirectUrl by lazy {
        intent?.getStringExtra(Constants.REDIRECT_URL)
    }

    private var loadingFinished = true
    private var redirect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_otp)
        setupToolbar()
        initViews()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        tv_toolbar_title?.text = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initViews() {
        setupWebView()
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

    private fun setupWebView() {
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

        redirectUrl?.let {
            web_view.loadUrl(it)
        }

    }

    private fun parseUrl(url: String?) {
        if (url.isNullOrEmpty()) return

        val uri = URI(url)
        val scheme = uri.scheme
        val host = uri.host

        Timber.tag("DEBUG").e("HOST::$host")

        if (!host.isNullOrEmpty()) {
            val query = uri.rawQuery
            val urlQuerySanitizer = UrlQuerySanitizer(url)
            val code = urlQuerySanitizer.getValue("code")

            Timber.tag("DEBUG").e("rawQuery:: $query")
            Timber.tag("DEBUG").e("urlQuerySanitizer:: $urlQuerySanitizer")
            Timber.tag("DEBUG").e("code:: $code")

            if (!code.isNullOrEmpty()) {
                val intent = Intent()
                intent.putExtra("GLOBE_CODE", code)
                setResult(Activity.RESULT_OK, intent)
                onBackPressed()
            }

//            val intent = Intent(this@LoginUbpActivity, CashInBankActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        }
    }

}