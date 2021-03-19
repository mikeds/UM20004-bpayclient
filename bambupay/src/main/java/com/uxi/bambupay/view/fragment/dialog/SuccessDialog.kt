package com.uxi.bambupay.view.fragment.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.uxi.bambupay.R
import com.uxi.bambupay.databinding.DialogMessageResultBinding
import com.uxi.bambupay.view.ext.viewBinding

/**
 * Created by Eraño Payawal on 10/4/20.
 * hunterxer31@gmail.com
 */
class SuccessDialog(
    private val ctx: Context,
    private val message: String?,
    private val amount: String?,
    private val date: String?,
    private val qrCodeUrl: String?,
    private val txFee: String? = null,
    private val onNewClicked: () -> Unit,
    private val onDashBoardClicked: () -> Unit,
    private val isTransactionVisible: Boolean? = true) : Dialog(ctx) {

    private val binding by viewBinding(DialogMessageResultBinding::inflate)

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_message_result)
        initViews()
    }

    private fun initViews() {
        message?.let {
            binding.textSuccessMessage.text = it
        }

        amount?.let {
            binding.textAmount.text = ctx.getString(R.string.amount_value, it)
        }

        date?.let {
            binding.textDate.text = it
        }

        if (!qrCodeUrl.isNullOrEmpty()) {
            binding.imageViewQrCode.visibility = View.VISIBLE
            loadImage(qrCodeUrl, binding.imageViewQrCode)
        }

        isTransactionVisible?.let {
            if (it) {
                binding.btnNewTransaction.visibility = View.VISIBLE
            } else {
                binding.btnNewTransaction.visibility = View.GONE
            }
        }

        if (txFee.isNullOrEmpty()) {
            binding.textFeeLbl.visibility = View.GONE
            binding.textFee.visibility = View.GONE
        }

        txFee?.let {
            binding.textFee.text = ctx.getString(R.string.amount_value, it)
        }

        binding.btnNewTransaction.setOnClickListener {
            dismiss()
            onNewClicked()
        }

        binding.btnOkay.setOnClickListener {
            dismiss()
            onDashBoardClicked()
        }
    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(ctx)
            .load(imageUrl)
            .thumbnail(1.0f)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

}