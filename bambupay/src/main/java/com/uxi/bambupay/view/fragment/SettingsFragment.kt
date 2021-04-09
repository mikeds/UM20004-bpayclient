package com.uxi.bambupay.view.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.uxi.bambupay.BuildConfig
import com.uxi.bambupay.R
import com.uxi.bambupay.view.activity.LoginActivity
import com.uxi.bambupay.view.activity.MainActivity
import com.uxi.bambupay.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun getLayoutId() = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_logout.setOnClickListener {
            showLogoutDialog()
        }

        if (context is MainActivity) {
            (context as MainActivity).setToolbarBgColor(R.color.white)
        }

        btn_data_privacy.setOnClickListener {
            val url = "${BuildConfig.API_BASE_URL_TMS}data-privacy"
            openBrowser(url)
        }

        btn_terms.setOnClickListener {
            val url = "${BuildConfig.API_BASE_URL_TMS}terms-and-conditions"
            openBrowser(url)
        }

        btn_eula.setOnClickListener {
            val url = "${BuildConfig.API_BASE_URL_TMS}eula"
            openBrowser(url)
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(context as Context)
        builder.setMessage(resources.getString(R.string.dialog_logout))
        builder.setNegativeButton(getString(R.string.action_cancel).toUpperCase(), null)
        builder.setPositiveButton(R.string.action_logout) { _, _ ->
            settingsViewModel.deleteLocalData()
            logout()
        }
        builder.create().show()
    }

    private fun logout() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun openBrowser(url: String?) {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(it)
            startActivity(intent)
        }
    }

}