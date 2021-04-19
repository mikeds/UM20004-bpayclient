package com.uxi.bambupay.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.uxi.bambupay.R
import com.uxi.bambupay.databinding.ActivityCreateQrcodeBinding
import com.uxi.bambupay.utils.Constants
import com.uxi.bambupay.view.ext.viewBinding
import com.uxi.bambupay.viewmodel.FeeViewModel
import com.uxi.bambupay.viewmodel.QRCodeViewModel
import com.uxi.bambupay.viewmodel.UserTokenViewModel
import kotlinx.android.synthetic.main.activity_create_qrcode.*
import timber.log.Timber

/**
 * Created by Eraño Payawal on 10/4/20.
 * hunterxer31@gmail.com
 */
class CreateQRActivity : BaseActivity() {

    private val qrCodeViewModel by viewModel<QRCodeViewModel>()
    private val userTokenModel by viewModel<UserTokenViewModel>()
    private val feeViewModel by viewModels<FeeViewModel> { viewModelFactory }
    private val binding by viewBinding(ActivityCreateQrcodeBinding::inflate)

    private val fromScreen by lazy {
        intent?.getStringExtra(Constants.SCREEN_FROM)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        initViews()
        events()
        observeViewModel()
    }

    override fun getLayoutId() = R.layout.activity_create_qrcode

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
        setSupportActionBar(binding.appToolbar.toolbar)
        binding.appToolbar.tvToolbarTitle.text = getString(R.string.create_qr)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun initViews() {
        binding.textFeeMsg.text = getString(R.string.convenience_fee_msg, "0")
    }

    private fun events() {
        binding.btnRetry.setOnClickListener {
            binding.textInputAmount.setText("")
            binding.imageViewQrCode.setImageDrawable(null)
            binding.containerButtons.visibility = View.GONE
            binding.containerGenerate.visibility = View.VISIBLE
        }

        binding.btnCancel.setOnClickListener {
            cancelCreateQR()
        }

        binding.btnCancelGenerate.setOnClickListener {
            cancelCreateQR()
        }

        binding.btnGenerate.setOnClickListener {
            it.hideKeyboard()
            binding.containerGenerate.visibility = View.GONE
            binding.containerButtons.visibility = View.VISIBLE
            qrCodeViewModel.subscribeCreatePayQr(binding.textInputAmount.text.toString())
        }

        binding.textInputAmount.doAfterTextChanged {
            feeViewModel.subscribeFee(it.toString(), Constants.TX_TYPE_CREATE_SCAN_QR_ID)
        }
    }

    private fun cancelCreateQR() {
        val intent = Intent(this, SelectPayQRActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK) // going back to previous screen and clear in between activity stack
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // clear all backstack
        startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
        finish()
    }

    private fun observeViewModel() {
        qrCodeViewModel.isAmountEmpty.observe(this, Observer {
            if (it) {
                showDialogMessage("Amount Required")
            }
        })

        qrCodeViewModel.createPayQrData.observe(this, Observer { scanQR ->
            scanQR?.let {
                binding.txtQrSuccessMsg.visibility = View.VISIBLE
                Timber.tag("DEBUG").e("create Qr code QR:: ${it.qrCode}")
                loadImage(it.qrCode!!, binding.imageViewQrCode)
            }
        })

        qrCodeViewModel.loading.observe(this, Observer {
            if (it) {
                showProgressDialog("Loading...")
            } else {
                dismissProgressDialog()
            }
        })

        qrCodeViewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (!isSuccess) {
                // call token refresher
                userTokenModel.subscribeToken()
            }
        })

        qrCodeViewModel.errorMessage.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                showMessageDialog(it)
            }
        })

        userTokenModel.isTokenRefresh.observe(this, Observer { isTokenRefresh ->
            if (isTokenRefresh) {
                qrCodeViewModel.subscribeCreatePayQr(binding.textInputAmount.text.toString())
            }
        })


        feeViewModel.fees.observe(this, Observer {
            it.getContentIfNotHandled()?.let { fee ->
                binding.textFee.text = fee
                binding.textFeeMsg.text = getString(R.string.convenience_fee_msg, fee)
            }
        })

    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(this@CreateQRActivity)
            .load(imageUrl)
            .thumbnail(1.0f)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

}